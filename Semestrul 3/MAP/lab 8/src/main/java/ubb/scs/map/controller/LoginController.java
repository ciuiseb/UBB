package ubb.scs.map.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ubb.scs.map.service.MySocialNetworkService;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ServiceException;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label errorLabel;

    private MySocialNetworkService service;
    private StageManager stageManager;
    private Stage stage;

    /**
     * Initializes the controller with required dependencies
     *
     * @param service      social network service
     * @param stageManager stage manager for scene switching
     * @param stage        current stage
     */
    public void setService(MySocialNetworkService service, StageManager stageManager, Stage stage) {
        this.service = service;
        this.stageManager = stageManager;
        this.stage = stage;

        // Set window title
        Platform.runLater(() -> stage.setTitle("Login"));
    }

    /**
     * Initialize FXML components and set up event handlers
     */
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(event -> handleLogin());
        signUpButton.setOnAction(event -> handleSignUp());

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleLogin();
            }
        });

        setupInputValidation();
    }

    /**
     * Handles the login button click event
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        if (!validateInput()) {
            return;
        }

        try {
            // Attempt login
            service.logIn(username, password)
                    .ifPresentOrElse(
                            this::handleSuccessfulLogin,
                            () -> showError("Invalid username or password")
                    );

        } catch (ServiceException e) {
            showError("Login error: " + e.getMessage());
        } catch (Exception e) {
            showError("An unexpected error occurred");
            e.printStackTrace();
        }
    }

    /**
     * Handles successful login attempts
     *
     * @param user authenticated user
     */
    private void handleSuccessfulLogin(User user) {
        try {
            if (user.isAdmin()) {
                stageManager.switchToAdminDashboard(user);
            } else {
                stageManager.switchToUserDashboard(user);
            }
        } catch (IOException e) {
            showError("Error loading dashboard");
            e.printStackTrace();
        }
    }

    /**
     * Handles the sign up button click event
     */
    private void handleSignUp() {
        try {
            stageManager.switchToSignUp();
        } catch (IOException e) {
            showError("Error loading sign up page");
            e.printStackTrace();
        }
    }

    /**
     * Sets up input validation for username and password fields
     */
    private void setupInputValidation() {
        // Add listeners for real-time validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
            errorLabel.setVisible(false);
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
            errorLabel.setVisible(false);
        });
    }

    /**
     * Validates user input
     *
     * @return true if input is valid, false otherwise
     */
    private boolean validateInput() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        usernameField.setStyle("");
        passwordField.setStyle("");

        boolean isValid = true;
        String errorStyle = "-fx-border-color: red;";

        if (username.isEmpty()) {
            usernameField.setStyle(errorStyle);
            showError("Username is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordField.setStyle(errorStyle);
            showError("Password is required");
            isValid = false;
        }

        // Enable/disable login button based on validation
        loginButton.setDisable(!isValid);

        return isValid;
    }

    /**
     * Shows error message to user
     *
     * @param message error message to display
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Creates a new instance of LoginController
     *
     * @param service      social network service
     * @param stageManager stage manager
     * @param stage        current stage
     * @return new LoginController instance
     * @throws IOException if FXML loading fails
     */
    public static LoginController create(MySocialNetworkService service,
                                         StageManager stageManager,
                                         Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LoginController.class.getResource("/views/login.fxml")
        );

        VBox root = loader.load();
        LoginController controller = loader.getController();
        controller.setService(service, stageManager, stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();

        return controller;
    }
}