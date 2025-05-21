package mpp.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import mpp.domain.Event;
import mpp.domain.Referee;
import mpp.domain.RefereeDiscipline;
import mpp.domain.Result;
import mpp.service.Service;

import java.io.IOException;
import java.util.*;

public class RaportViewController {
    @FXML
    private TableView<Map<String, Object>> resultsTable;
    @FXML
    private Button backButton;
    @FXML
    private Label eventLabel;

    private Service service;
    private Referee referee;
    private Event refereeEvent;

    @FXML
    public void initialize() {
        backButton.setOnAction(this::handleBackButton);
    }

    public void loadEvents() {
        List<RefereeDiscipline> assignments = service.getRefereeDisciplines(referee.getId());

        if (assignments != null && !assignments.isEmpty()) {
            refereeEvent = assignments.getFirst().getEvent();
            eventLabel.setText("Event: " + refereeEvent.getName());
            displayResults(refereeEvent);
        } else {
            eventLabel.setText("No assigned events");
        }
    }

    private void displayResults(Event event) {
        resultsTable.getColumns().clear();

        TableColumn<Map<String, Object>, String> nameCol = new TableColumn<>("Participant");
        TableColumn<Map<String, Object>, Integer> totalPointsCol = new TableColumn<>("Total Points");

        nameCol.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("name")));
        totalPointsCol.setCellValueFactory(data -> new SimpleIntegerProperty((int) data.getValue().get("totalPoints")).asObject());

        resultsTable.getColumns().addAll(nameCol, totalPointsCol);

        Map<String, Map<String, Object>> participantResults = new HashMap<>();

        List<Result> eventResults = service.getResultsByEvent(event.getId());

        for (Result r : eventResults) {
            String name = r.getParticipant().getName();


            participantResults.putIfAbsent(name, new HashMap<>());
            Map<String, Object> participantData = participantResults.get(name);

            participantData.put("name", name);

            int currentTotal = (int) participantData.getOrDefault("totalPoints", 0);
            participantData.put("totalPoints", currentTotal + r.getPoints());
        }

        List<Map<String, Object>> sortedResults = new ArrayList<>(participantResults.values());
        sortedResults.sort((p1, p2) -> {
            int points1 = (int) p1.get("totalPoints");
            int points2 = (int) p2.get("totalPoints");
            return Integer.compare(points2, points1);
        });

        resultsTable.getItems().setAll(sortedResults);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RefereeDashboard.fxml"));
            Parent dashboardRoot = loader.load();

            RefereeDashboardController dashboardController = loader.getController();
            dashboardController.setService(service);
            dashboardController.setReferee(referee);

            Scene dashboardScene = new Scene(dashboardRoot);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Referee Dashboard - " + referee.getName());
            stage.setScene(dashboardScene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}