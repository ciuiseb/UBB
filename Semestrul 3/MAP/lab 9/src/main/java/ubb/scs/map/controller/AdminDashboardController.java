package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.entities.UserType;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.service.MySocialNetworkService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminDashboardController {
    private MySocialNetworkService service;
    private StageManager stageManager;
    private Stage stage;
    private User currentAdmin;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button banUserButton;
    @FXML
    private Button unBanUserButton;
    @FXML
    private Button logoutButton;

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Long> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, UserType> rankColumn;

    private final ObservableList<User> usersList = FXCollections.observableArrayList();

    public void setService(MySocialNetworkService service, StageManager stageManager, Stage stage, User admin) {
        this.service = service;
        this.stageManager = stageManager;
        this.stage = stage;
        this.currentAdmin = admin;
        loadUsers();
    }

    @FXML
    public void initialize() {
        setupTable();
        setupButtons();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

        usersTable.setItems(usersList);

        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            deleteUserButton.setDisable(!hasSelection);
            banUserButton.setDisable(!hasSelection);
            unBanUserButton.setDisable(!hasSelection);
        });
    }

    private void setupButtons() {
        searchButton.setOnAction(event -> handleSearch());
        deleteUserButton.setOnAction(event -> handleDeleteUser());
        banUserButton.setOnAction(event -> handleBanUser());
        unBanUserButton.setOnAction(event -> handleUnBanUser());
        logoutButton.setOnAction(event -> handleLogout());

        deleteUserButton.setDisable(true);
        banUserButton.setDisable(true);
        unBanUserButton.setDisable(true);
    }

    private void loadUsers() {
        try {
            List<User> users = StreamSupport
                    .stream(service.getAllUsers().spliterator(), false)
                    .filter(u -> !u.getUsername().equals(currentAdmin.getUsername()))
                    .collect(Collectors.toList());
            usersList.setAll(users);
        } catch (ServiceException e) {
            Utils.showError("Error loading users: " + e.getMessage());
        }
    }

    private void handleSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadUsers();
            return;
        }

        String[] parts = searchText.split(" ", 2);
        String firstName = parts[0].toLowerCase();
        String lastName = parts.length > 1 ? parts[1].toLowerCase() : "";

        List<User> filteredUsers = usersList.stream()
                .filter(user -> {
                    boolean firstNameMatch = user.getFirstName().toLowerCase().contains(firstName);
                    boolean lastNameMatch = lastName.isEmpty() ||
                            user.getLastName().toLowerCase().contains(lastName);
                    return firstNameMatch && lastNameMatch;
                })
                .collect(Collectors.toList());

        usersList.setAll(filteredUsers);
    }

    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete " +
                selectedUser.getFirstName() + " " +
                selectedUser.getLastName() + "?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    service.deleteUser(selectedUser.getId());
                    refresh();
                } catch (ServiceException e) {
                    Utils.showError("Error deleting user: " + e.getMessage());
                }
            }
        });
    }

    private void handleBanUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;

        service.banUser(selectedUser.getId());
        refresh();
    }

    private void handleUnBanUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;

        service.unBanUser(selectedUser.getId());
        refresh();
    }

    private void handleLogout() {
        try {
            service.logout();
            stageManager.switchToLogin();
        } catch (Exception e) {
            Utils.showError("Error during logout: " + e.getMessage());
        }
    }

    private void refresh() {
        loadUsers();
    }
}