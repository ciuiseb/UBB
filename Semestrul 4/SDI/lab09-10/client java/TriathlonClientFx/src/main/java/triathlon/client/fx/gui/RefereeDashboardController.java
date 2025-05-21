package triathlon.client.fx.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import triathlon.model.*;
import triathlon.services.ITriathlonObserver;
import triathlon.services.ITriathlonServices;
import triathlon.services.TriathlonException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefereeDashboardController implements ITriathlonObserver {

    // FXML controls
    @FXML private Label refereeNameLabel, disciplineLabel, eventLabel;
    @FXML private Button logoutButton, updateScoreButton;
    @FXML private TableView<Result> disciplineScoresTable, totalPointsTable;
    @FXML private TableColumn<Result, String> disciplineParticipantColumn, totalPointsParticipantColumn;
    @FXML private TableColumn<Result, Integer> disciplineScoreColumn, totalPointsColumn;
    @FXML private RadioButton allParticipantsRadio, ungradedParticipantsRadio;
    @FXML private ToggleGroup participantFilterGroup;
    @FXML private TextField scoreField;

    // Model data
    private ITriathlonServices server;
    private Referee referee;
    private Event currentEvent;
    private Discipline currentDiscipline;
    private RefereeDiscipline currentAssignment;

    // Observable collections
    private final ObservableList<Result> resultsData = FXCollections.observableArrayList();
    private final ObservableList<Result> totalPointsData = FXCollections.observableArrayList();
    private FilteredList<Result> filteredResultsData;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupListeners();

        // Initialize filtered list
        filteredResultsData = new FilteredList<>(resultsData, r -> true);
        disciplineScoresTable.setItems(filteredResultsData);

        // Setup filter radio buttons
        allParticipantsRadio.setSelected(true);
        allParticipantsRadio.setOnAction(e -> filteredResultsData.setPredicate(r -> true));
        ungradedParticipantsRadio.setOnAction(e -> filteredResultsData.setPredicate(r -> r.getPoints() <= 0));
    }

    private void setupTableColumns() {
        // Setup columns for discipline scores table
        disciplineParticipantColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipant().getName()));
        disciplineScoreColumn.setCellValueFactory(
                new PropertyValueFactory<>("points"));

        // Setup columns for total points table
        totalPointsParticipantColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipant().getName()));
        totalPointsColumn.setCellValueFactory(cellData -> {
            Participant p = cellData.getValue().getParticipant();
            return new SimpleIntegerProperty(
                    calculateTotalPoints(p)).asObject();
        });

        totalPointsTable.setItems(totalPointsData);
    }

    private void setupListeners() {
        // Setup button actions
        updateScoreButton.setOnAction(e -> updateSelectedResult());
        logoutButton.setOnAction(e -> logout());

        // Setup selection listener for updating score field
        disciplineScoresTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                scoreField.setText(String.valueOf(newSel.getPoints()));
            }
        });
    }

    public void setServer(ITriathlonServices server) {
        this.server = server;
    }

    public void setAssignement(RefereeDiscipline assignment) {
        this.currentAssignment = assignment;
        this.referee = assignment.getReferee();
        this.currentEvent = assignment.getEvent();
        this.currentDiscipline = assignment.getDiscipline();

        // Update UI labels
        refereeNameLabel.setText(referee.getUsername());
        eventLabel.setText(currentEvent.getName());
        disciplineLabel.setText(currentDiscipline.name());

        loadResults();
    }

    private void loadResults() {
        try {
            if (currentEvent == null) return;

            // Remember the currently selected result if any
            Result selectedResult = disciplineScoresTable.getSelectionModel().getSelectedItem();
            Long selectedId = selectedResult != null ? selectedResult.getId() : null;

            // Get fresh data from server
            List<Result> allResults = server.getResultsByEvent(currentEvent.getId());

            // Preserve existing filter state
            boolean showOnlyUngraded = ungradedParticipantsRadio.isSelected();

            // 1. First, update the discipline-specific table
            List<Result> disciplineResults = allResults.stream()
                    .filter(r -> r.getDiscipline() == currentDiscipline)
                    .toList();

            // Clear and then add all to ensure we have fresh instances
            resultsData.clear();
            resultsData.addAll(disciplineResults);

            // 2. Update the total points table with ALL participants
            // This ensures we never lose participants from the view
            Map<Long, Result> uniqueParticipants = new HashMap<>();
            for (Result r : allResults) {
                // Keep track of unique participants by using the first result we find for them
                uniqueParticipants.putIfAbsent(r.getParticipant().getId(), r);
            }

            // Update total points table
            totalPointsData.clear();
            totalPointsData.addAll(uniqueParticipants.values());

            // Restore selection if it still exists in the new data
            if (selectedId != null) {
                for (int i = 0; i < resultsData.size(); i++) {
                    if (resultsData.get(i).getId().equals(selectedId)) {
                        disciplineScoresTable.getSelectionModel().select(i);
                        break;
                    }
                }
            } else if (!resultsData.isEmpty()) {
                // Otherwise select first result by default
                disciplineScoresTable.getSelectionModel().selectFirst();
            }

            // Reapply filter if needed
            if (showOnlyUngraded) {
                filteredResultsData.setPredicate(r -> r.getPoints() <= 0);
            } else {
                filteredResultsData.setPredicate(r -> true);
            }

            // Refresh tables to ensure data is properly displayed
            Platform.runLater(() -> {
                disciplineScoresTable.refresh();
                totalPointsTable.refresh();
            });

        } catch (Exception e) {
            Util.showError("Error loading results: " + e.getMessage());
        }
    }

    private int calculateTotalPoints(Participant participant) {
        try {
            return (currentEvent == null) ? 0 :
                    server.getTotalPointsForParticipant(participant.getId(), currentEvent.getId());
        } catch (Exception e) {
            return 0;
        }
    }

    private void updateSelectedResult() {
        Result selected = disciplineScoresTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.showError("Selection Error: Please select a result to update");
            return;
        }

        try {
            // Parse and validate the new score
            String scoreText = scoreField.getText().trim();
            if (scoreText.isEmpty()) {
                Util.showError("Please enter a score");
                return;
            }

            int newPoints = Integer.parseInt(scoreText);
            if (newPoints < 0) {
                Util.showError("Score cannot be negative");
                return;
            }

            // Clone the result to avoid concurrent modification issues
            Result updatedResult = new Result(
                    selected.getId(),
                    selected.getEvent(),
                    selected.getParticipant(),
                    selected.getDiscipline(),
                    newPoints
            );

            // Send update to server
            server.updateResult(updatedResult);

            // Don't update the UI here - wait for server notification
            // This ensures all clients are in sync
            Util.showAlert("Success", "Result updated successfully");

        } catch (NumberFormatException e) {
            Util.showError("Please enter a valid score");
        } catch (Exception e) {
            Util.showError("Error updating result: " + e.getMessage());
        }
    }

    private void logout() {
        try {
            server.logout(referee, this);
            logoutButton.getScene().getWindow().hide();
        } catch (TriathlonException e) {
            Util.showError("Error during logout: " + e.getMessage());
        }
    }

    @Override
    public void resultsUpdated(Result result) throws TriathlonException {
        // This method is called by the server when any client updates a result
        // It needs to safely update the UI on the JavaFX thread
        Platform.runLater(() -> {
            try {
                // We need to completely reload from the server to ensure consistency
                loadResults();

                if (currentEvent != null &&
                        result.getEvent().getId().equals(currentEvent.getId()) &&
                        result.getDiscipline() == currentDiscipline) {

                    for (int i = 0; i < resultsData.size(); i++) {
                        Result r = resultsData.get(i);
                        if (r.getId().equals(result.getId())) {
                            disciplineScoresTable.getSelectionModel().select(i);
                            disciplineScoresTable.scrollTo(i);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error refreshing results: " + e.getMessage());
            }
        });
    }
}