package mpp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import mpp.domain.*;
import mpp.service.Service;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class RefereeDashboardController {

    @FXML
    private Label disciplineLabel;

    @FXML
    private Button raportButton;

    @FXML
    private TableView<Result> resultsTable;

    @FXML
    private TableColumn<Result, String> participantColumn;

    @FXML
    private TableColumn<Result, Integer> scoreColumn;

    @FXML
    private RadioButton allParticipantsRadio;

    @FXML
    private RadioButton ungradedParticipantsRadio;

    @FXML
    private ToggleGroup participantFilterGroup;


    private Service service;
    private Referee currentReferee;
    private RefereeDiscipline currentAssignment;
    private ObservableList<Result> resultsData = FXCollections.observableArrayList();
    private FilteredList<Result> filteredResultsData;

    @FXML
    public void initialize() {
        participantColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue().getParticipant();
            return javafx.beans.binding.Bindings.createStringBinding(() -> participant.getName());
        });

        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        resultsTable.setEditable(true);
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        scoreColumn.setOnEditCommit(event -> {
            Result result = event.getRowValue();
            result.setPoints(event.getNewValue());
            service.updateResult(result);
        });

        raportButton.setOnAction(this::openAllEventsView);

        filteredResultsData = new FilteredList<>(resultsData);
        resultsTable.setItems(filteredResultsData);

        allParticipantsRadio.setOnAction(event -> updateFilter());
        ungradedParticipantsRadio.setOnAction(event -> updateFilter());
    }

    private void updateFilter() {
        if (ungradedParticipantsRadio.isSelected()) {
            filteredResultsData.setPredicate(result -> result.getPoints() <= 0);
        } else {
            filteredResultsData.setPredicate(result -> true);
        }
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setReferee(Referee referee) {
        this.currentReferee = referee;
        loadRefereeAssignment();
    }

    private void loadRefereeAssignment() {
        List<RefereeDiscipline> assignments = service.getRefereeDisciplines(currentReferee.getId());

        if (assignments != null && !assignments.isEmpty()) {
            currentAssignment = assignments.get(0);

            Event event = currentAssignment.getEvent();
            Discipline discipline = currentAssignment.getDiscipline();

            disciplineLabel.setText(event.getName() + " - " + discipline.name());

            loadResults(event, discipline);
        } else {
            disciplineLabel.setText("No assignment");
            resultsData.clear();
        }
    }

    private void loadResults(Event event, Discipline discipline) {
        List<Result> eventResults = service.getResultsByEvent(event.getId());

        List<Result> disciplineResults = eventResults.stream()
                .filter(r -> r.getDiscipline() == discipline)
                .toList();

        resultsData.clear();
        resultsData.addAll(disciplineResults);
        updateFilter();

    }

    private void updateScores() {
        for (Result result : resultsData) {
            service.updateResult(result);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scores Saved");
        alert.setHeaderText(null);
        alert.setContentText("Points have been successfully saved.");
        alert.showAndWait();
    }

    private void openAllEventsView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RaportView.fxml"));
            Parent eventsRoot = loader.load();

            RaportViewController controller = loader.getController();
            controller.setService(service);
            controller.setReferee(currentReferee);
            controller.loadEvents();

            Scene eventsScene = new Scene(eventsRoot);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("All Events");
            stage.setScene(eventsScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Could not open Events view: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}