package triathlon.client.fx.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import triathlon.model.Referee;
import triathlon.services.ITriathlonServices;
import triathlon.services.TriathlonException;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private ITriathlonServices server;

    public void setServer(ITriathlonServices server) {
        this.server = server;
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(this::handleLogin);
    }

    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Util.showAlert("Login Error", "Please fill in both fields.");
            return;
        }

        try {
            Referee referee = new Referee();
            referee.setUsername(username);
            referee.setPassword(password);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RefereeDashboard.fxml"));
            Parent root = loader.load();

            RefereeDashboardController dashboardController = loader.getController();

            var asssignement = server.login(referee, dashboardController);

            dashboardController.setServer(server);
            dashboardController.setAssignement(asssignement);

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Referee Dashboard");
            dashboardStage.setScene(new Scene(root));

            dashboardStage.show();
            ((Node) event.getSource()).getScene().getWindow().hide();

        } catch (TriathlonException e) {
            Util.showAlert("Login Failed", "Invalid username or password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}