package ubb.scs.map.controller.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.service.MySocialNetworkService;

public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backToLoginButton;
    @FXML
    private Label errorLabel;

    private MySocialNetworkService service;
    private StageManager stageManager;
    private Stage stage;

    public void setService(MySocialNetworkService service, StageManager stageManager, Stage stage) {
        this.service = service;
        this.stageManager = stageManager;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
//        setupValidation();
        setupButtons();
        errorLabel.setVisible(false);
    }

    private void setupValidation() {
        usernameField.textProperty().addListener((obs, oldText, newText) -> validateFields());
        firstNameField.textProperty().addListener((obs, oldText, newText) -> validateFields());
        lastNameField.textProperty().addListener((obs, oldText, newText) -> validateFields());
        passwordField.textProperty().addListener((obs, oldText, newText) -> validateFields());
        confirmPasswordField.textProperty().addListener((obs, oldText, newText) -> validateFields());
    }

    private void setupButtons() {
        signUpButton.setOnAction(event -> handleSignUp());
        backToLoginButton.setOnAction(event -> handleBackToLogin());
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText();

        try {
            if(!validateFields())
                return;
            User newUser = new User(username, firstName, lastName, password, "user");
            service.addUser(newUser);
            Utils.showInfo("Account created successfully!");
            stageManager.switchToLogin();
        } catch (ServiceException e) {
            Utils.showError(e.getMessage());
        } catch (Exception e) {
            Utils.showError("An unexpected error occurred");
        }
    }

    private void handleBackToLogin() {
        try {
            stageManager.switchToLogin();
        } catch (Exception e) {
            Utils.showError("Error returning to login");
        }
    }

    private boolean validateFields() {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        errorLabel.setVisible(false);

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            Utils.showError("All fields are required");
            return false;
        }

        if (username.length() < 3) {
            Utils.showError("Username must be at least 3 characters");
            return false;
        }

        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            Utils.showError("Names should only contain letters");
            return false;
        }

        if (password.length() < 6) {
            Utils.showError("Password must be at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Utils.showError("Passwords do not match");
            return false;
        }

//        signUpButton.setDisable(false);
        return true;
    }
}
