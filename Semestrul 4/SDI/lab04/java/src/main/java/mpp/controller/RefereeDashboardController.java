package mpp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import mpp.domain.*;
import mpp.service.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RefereeDashboardController {

    @FXML
    private Label disciplineLabel;

    @FXML
    private Button allEventsButton;

    @FXML
    private TableView<Result> resultsTable;

    @FXML
    private TableColumn<Result, String> participantColumn;

    @FXML
    private TableColumn<Result, Integer> scoreColumn;

    @FXML
    private Button saveScoresButton;

    private Service service;
    private Referee currentReferee;
    private RefereeDiscipline currentAssignment;
    private ObservableList<Result> resultsData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        participantColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue().getParticipant();
            return javafx.beans.binding.Bindings.createStringBinding(() -> participant.getName());
        });

        // Set up points column using PropertyValueFactory
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        // Allow editing of points
        resultsTable.setEditable(true);
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        scoreColumn.setOnEditCommit(event -> {
            Result result = event.getRowValue();
            result.setPoints(event.getNewValue());
        });

        // Set up button actions
        saveScoresButton.setOnAction(event -> saveScores());
        allEventsButton.setOnAction(event -> openAllEventsView());

        // Set up the table data
        resultsTable.setItems(resultsData);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setReferee(Referee referee) {
        this.currentReferee = referee;
        loadRefereeAssignment();
    }

    private void loadRefereeAssignment() {
        // Get the referee's current assignment
        List<RefereeDiscipline> assignments = service.getRefereeDisciplines(currentReferee.getId());

        if (assignments != null && !assignments.isEmpty()) {
            // For simplicity, we'll take the first assignment
            currentAssignment = assignments.get(0);

            // Get the event and discipline details from the assignment
            Event event = currentAssignment.getEvent();
            Discipline discipline = currentAssignment.getDiscipline();

            // Update UI with the discipline name
            disciplineLabel.setText(event.getName() + " - " + discipline.name());

            // Load participants and their results for this event/discipline
            loadResults(event, discipline);
        } else {
            disciplineLabel.setText("No assignment");
            resultsData.clear();
            saveScoresButton.setDisable(true);
        }
    }

    private void loadResults(Event event, Discipline discipline) {
        // Get all results for this event
        List<Result> eventResults = service.getResultsByEvent(event.getId());

        // Filter for the current discipline
        List<Result> disciplineResults = eventResults.stream()
                .filter(r -> r.getDiscipline() == discipline)
                .collect(Collectors.toList());

        // Update table
        resultsData.clear();
        resultsData.addAll(disciplineResults);

        // Enable save button if we have results to update
        saveScoresButton.setDisable(disciplineResults.isEmpty());
    }

    private void saveScores() {
        // Save all results in the table
        for (Result result : resultsData) {
            service.updateResult(result);
        }

        // Show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scores Saved");
        alert.setHeaderText(null);
        alert.setContentText("Points have been successfully saved.");
        alert.showAndWait();
    }

    private void openAllEventsView() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AllEvents.fxml"));
//            Scene scene = new Scene(loader.load());
//
//            // Get the controller and set necessary data
//            AllEventsController controller = loader.getController();
//            controller.setService(service);
//            controller.setReferee(currentReferee);
//
//            // Create and show the new stage
//            Stage eventsStage = new Stage();
//            eventsStage.setTitle("All Events");
//            eventsStage.setScene(scene);
//            eventsStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showError("Could not open Events view: " + e.getMessage());
//        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}