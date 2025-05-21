package mpp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import mpp.domain.Referee;
import mpp.service.Service;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(this::handleLogin);
    }

    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please fill in both fields.");
            return;
        }

        Referee referee = service.login(username, password);

        if (referee != null) {
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
                showAlert("Could not open dashboard: " + e.getMessage());
            }
        } else {
            showAlert("Invalid credentials. Please try again.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
