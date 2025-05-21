package mpp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mpp.controller.LoginController;
import mpp.repository.impl.*;
import mpp.service.Service;
import mpp.service.ServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp extends Application {

    private static final Logger log = LogManager.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        var eventRepository = new EventRepositoryImpl();
        var participantrepository = new ParticipantRepositoryImpl();
        var resultrepository = new ResultRepositoryImpl(eventRepository, participantrepository);
        var refereerepository = new RefereeRepositoryImpl();
        var refereeDisciplineRepository = new RefereeDisciplineRepositoryImpl(refereerepository, eventRepository);
        var service = new ServiceImpl(eventRepository, participantrepository, refereerepository, resultrepository, refereeDisciplineRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Triathlon Referee Login");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}