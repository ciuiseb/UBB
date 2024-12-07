package ubb.scs.map.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.service.MySocialNetworkService;

import java.io.IOException;

public class StageManager {
    private final Stage primaryStage;
    private final MySocialNetworkService service;
    private Scene currentScene;

    public StageManager(Stage primaryStage, MySocialNetworkService service) {
        this.primaryStage = primaryStage;
        this.service = service;
        primaryStage.setTitle("Social Network");
        primaryStage.setResizable(true);
    }

    public void switchToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Pane root = loader.load();

        LoginController controller = loader.getController();
        controller.setService(service, this, primaryStage);

        if (currentScene == null) {
            currentScene = new Scene(root, 400, 500);
        } else {
            currentScene.setRoot(root);
        }

        primaryStage.setScene(currentScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void switchToSignUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signup.fxml"));
        Pane root = loader.load();

        SignUpController controller = loader.getController();
        controller.setService(service, this, primaryStage);

        currentScene.setRoot(root);
        primaryStage.centerOnScreen();
    }

    public void switchToUserDashboard(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user.fxml"));
        Pane root = loader.load();

        UserDashboardController controller = loader.getController();
        controller.setService(service, this, primaryStage, user);

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        currentScene.setRoot(root);
//        primaryStage.centerOnScreen();
    }

    public void switchToAdminDashboard(User admin) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin.fxml"));
        Pane root = loader.load();

        AdminDashboardController controller = loader.getController();
        controller.setService(service, this, primaryStage, admin);

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        currentScene.setRoot(root);
        primaryStage.centerOnScreen();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}