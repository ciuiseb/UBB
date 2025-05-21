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
import java.util.stream.Collectors;

public class RefereeDashboardController implements ITriathlonObserver {

    
    @FXML private Label refereeNameLabel, disciplineLabel, eventLabel;
    @FXML private Button logoutButton, updateScoreButton;
    @FXML private TableView<Result> disciplineScoresTable, totalPointsTable;
    @FXML private TableColumn<Result, String> disciplineParticipantColumn, totalPointsParticipantColumn;
    @FXML private TableColumn<Result, Integer> disciplineScoreColumn, totalPointsColumn;
    @FXML private RadioButton allParticipantsRadio, ungradedParticipantsRadio;
    @FXML private ToggleGroup participantFilterGroup;
    @FXML private TextField scoreField;

    
    private ITriathlonServices server;
    private Referee referee;
    private Event currentEvent;
    private Discipline currentDiscipline;
    private RefereeDiscipline currentAssignment;

    
    private final ObservableList<Result> resultsData = FXCollections.observableArrayList();
    private final ObservableList<Result> totalPointsData = FXCollections.observableArrayList();
    private FilteredList<Result> filteredResultsData;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupListeners();

        
        filteredResultsData = new FilteredList<>(resultsData, r -> true);
        disciplineScoresTable.setItems(filteredResultsData);

        
        allParticipantsRadio.setSelected(true);
        allParticipantsRadio.setOnAction(e -> filteredResultsData.setPredicate(r -> true));
        ungradedParticipantsRadio.setOnAction(e -> filteredResultsData.setPredicate(r -> r.getPoints() <= 0));
    }

    private void setupTableColumns() {
        
        disciplineParticipantColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipant().getName()));
        disciplineScoreColumn.setCellValueFactory(
                new PropertyValueFactory<>("points"));

        
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
        
        updateScoreButton.setOnAction(e -> updateSelectedResult());
        logoutButton.setOnAction(e -> logout());

        
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

        
        refereeNameLabel.setText(referee.getUsername());
        eventLabel.setText(currentEvent.getName());
        disciplineLabel.setText(currentDiscipline.name());

        loadResults();
    }

    private void loadResults() {
        try {
            if (currentEvent == null) return;

            
            Result selectedResult = disciplineScoresTable.getSelectionModel().getSelectedItem();
            Long selectedId = selectedResult != null ? selectedResult.getId() : null;

            
            List<Result> allResults = server.getResultsByEvent(currentEvent.getId());

            
            boolean showOnlyUngraded = ungradedParticipantsRadio.isSelected();

            
            List<Result> disciplineResults = allResults.stream()
                    .filter(r -> r.getDiscipline() == currentDiscipline)
                    .toList();

            
            resultsData.clear();
            resultsData.addAll(disciplineResults);

            
            
            Map<Long, Result> uniqueParticipants = new HashMap<>();
            for (Result r : allResults) {
                
                uniqueParticipants.putIfAbsent(r.getParticipant().getId(), r);
            }

            
            totalPointsData.clear();
            totalPointsData.addAll(uniqueParticipants.values());

            
            if (selectedId != null) {
                for (int i = 0; i < resultsData.size(); i++) {
                    if (resultsData.get(i).getId().equals(selectedId)) {
                        disciplineScoresTable.getSelectionModel().select(i);
                        break;
                    }
                }
            } else if (!resultsData.isEmpty()) {
                
                disciplineScoresTable.getSelectionModel().selectFirst();
            }

            
            if (showOnlyUngraded) {
                filteredResultsData.setPredicate(r -> r.getPoints() <= 0);
            } else {
                filteredResultsData.setPredicate(r -> true);
            }

            
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

            
            Result updatedResult = new Result(
                    selected.getId(),
                    selected.getEvent(),
                    selected.getParticipant(),
                    selected.getDiscipline(),
                    newPoints
            );

            
            server.updateResult(updatedResult);

            
            
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
        
        
        Platform.runLater(() -> {
            try {
                
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