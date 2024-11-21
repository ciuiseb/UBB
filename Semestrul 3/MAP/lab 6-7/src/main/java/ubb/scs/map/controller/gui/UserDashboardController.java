package ubb.scs.map.controller.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ubb.scs.map.domain.entities.FriendshipRequest;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.entities.UserType;
import ubb.scs.map.service.MySocialNetworkService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserDashboardController {
    private MySocialNetworkService service;
    private User currentUser;
    private Stage stage;
    private StageManager stageManager;

    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<User> friendsTable;
    @FXML
    private TableColumn<User, String> friendFirstNameColumn;
    @FXML
    private TableColumn<User, String> friendLastNameColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<User> searchResultsTable;
    @FXML
    private TableColumn<User, String> searchFirstNameColumn;
    @FXML
    private TableColumn<User, String> searchLastNameColumn;

    @FXML
    private TableView<FriendshipRequest> requestsTable;
    @FXML
    private TableColumn<FriendshipRequest, String> requestFirstNameColumn;
    @FXML
    private TableColumn<FriendshipRequest, String> requestLastNameColumn;
    @FXML
    private TableColumn<FriendshipRequest, String> requestDateColumn;

    @FXML
    private Button logoutButton;
    @FXML
    private Button removeFriendButton;
    @FXML
    private Button sendRequestButton;
    @FXML
    private Button acceptRequestButton;
    @FXML
    private Button rejectRequestButton;
    private final ObservableList<User> friendsList = FXCollections.observableArrayList();
    private final ObservableList<User> searchResultsList = FXCollections.observableArrayList();
    private final ObservableList<FriendshipRequest> requestsList = FXCollections.observableArrayList();

    public void setService(MySocialNetworkService service, StageManager stageManager, Stage stage, User user) {
        this.service = service;
        this.stageManager = stageManager;
        this.stage = stage;
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getFirstName() + " " + user.getLastName());
        initializeData();
    }

    @FXML
    public void initialize() {
        setupFriendsTable();
        setupSearchTable();
        setupRequestsTable();
        setupButtons();
    }

    private void setupFriendsTable() {
        friendFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsTable.setItems(friendsList);

        friendsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> removeFriendButton.setDisable(newSelection == null)
        );
    }

    private void setupSearchTable() {
        searchFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        searchLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        searchResultsTable.setItems(searchResultsList);

        searchResultsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> sendRequestButton.setDisable(newSelection == null)
        );
    }

    private void setupRequestsTable() {
        requestFirstNameColumn.setCellValueFactory(cellData -> {
            User sender = service.getUser(cellData.getValue().getFirst());
            return new SimpleStringProperty(sender.getFirstName());
        });

        requestLastNameColumn.setCellValueFactory(cellData -> {
            User sender = service.getUser(cellData.getValue().getSecond());
            return new SimpleStringProperty(sender.getLastName());
        });

        requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("sentDate"));

        requestDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        ));

        requestsTable.setItems(requestsList);
        requestsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    acceptRequestButton.setDisable(newSelection == null);
                    rejectRequestButton.setDisable(newSelection == null);
                }
        );
    }


    private void setupButtons() {
        searchButton.setOnAction(event -> handleSearch());
        logoutButton.setOnAction(event -> handleLogout());

        removeFriendButton.setOnAction(event -> handleRemoveFriend());
        sendRequestButton.setOnAction(event -> handleSendRequest());
        acceptRequestButton.setOnAction(event -> handleAcceptRequest());
        rejectRequestButton.setOnAction(event -> handleRejectRequest());

        removeFriendButton.setDisable(true);
        acceptRequestButton.setDisable(true);
        rejectRequestButton.setDisable(true);
    }


    private void initializeData() {
        refresh();
    }

    private void loadFriends() {
        List<User> friends = StreamSupport
                .stream(service.getAllFriendships().spliterator(), false)
                .filter(f -> f.getFirst().equals(currentUser.getId()) ||
                        f.getSecond().equals(currentUser.getId()))
                .map(f -> {
                    Long friendId = f.getFirst().equals(currentUser.getId()) ?
                            f.getSecond() : f.getFirst();
                    return service.getUser(friendId);
                })
                .filter(u -> u.getUserType().equals(UserType.USER))
                .toList();
        friendsList.setAll(friends);
    }

    private void loadSearchUsers() {
        List<User> users = StreamSupport
                .stream(service.getAllUsers().spliterator(), false)
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .filter(u -> u.getUserType().equals(UserType.USER))
                .filter(u -> friendsList.stream().noneMatch(f -> f.getId().equals(u.getId())))
                .limit(10)
                .collect(Collectors.toList());

        searchResultsList.setAll(users);
    }

    private void loadFriendRequests() {
        Iterable<FriendshipRequest> requests = service.getFriendshipRequests(currentUser);
        List<FriendshipRequest> requestList = new ArrayList<>();
        requests.forEach(requestList::add);
        requestsList.setAll(requestList);
    }

    private void handleSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refresh();
            return;
        }

        String[] parts = searchText.split(" ", 2);
        String firstName = parts[0].toLowerCase();
        String lastName = parts.length > 1 ? parts[1].toLowerCase() : "";

        List<User> filteredUsers = searchResultsList.stream()
                .filter(user -> {
                    boolean firstNameMatch = user.getFirstName().toLowerCase().contains(firstName);
                    boolean lastNameMatch = lastName.isEmpty() ||
                            user.getLastName().toLowerCase().contains(lastName);
                    return firstNameMatch && lastNameMatch;
                })
                .collect(Collectors.toList());

        searchResultsList.setAll(filteredUsers);
        refresh();
    }

    private void handleSendRequest() {
        User selectedUser = searchResultsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                service.addFriendshipRequest(currentUser.getId(), selectedUser.getId());
                sendRequestButton.setDisable(true);
                Utils.showInfo("Friend request sent!");
                refresh();
            } catch (Exception e) {
                Utils.showError("Error sending friend request: " + e.getMessage());
            }
        }
    }

    private void handleRemoveFriend() {
        User targetFriend = friendsTable.getSelectionModel().getSelectedItem();
        if (targetFriend == null)
            return;
        service.deleteFriendship(currentUser.getId(), targetFriend.getId());
        refresh();
    }

    private void handleAcceptRequest() {
        FriendshipRequest selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            try {
                service.acceptFriendshipRequest(selectedRequest);
                Utils.showInfo("Friend request accepted!");
                refresh();
            } catch (Exception e) {
                Utils.showError("Error accepting request: " + e.getMessage());
            }
        }
    }

    private void handleRejectRequest() {
        FriendshipRequest selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            try {
                service.denyFriendshipRequest(selectedRequest);
                Utils.showInfo("Friend request rejected.");
                refresh();
            } catch (Exception e) {
                Utils.showError("Error rejecting request: " + e.getMessage());
            }
        }
    }

    private void handleLogout() {
        service.logout();
        try {
            stageManager.switchToLogin();
        } catch (Exception e) {
            Utils.showError("Error during logout: " + e.getMessage());
        }
    }
    private void refresh(){
        loadFriends();
        loadFriendRequests();
        loadSearchUsers();
    }
}