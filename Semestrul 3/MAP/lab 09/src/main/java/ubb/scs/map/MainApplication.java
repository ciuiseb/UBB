package ubb.scs.map;

import javafx.application.Application;
import javafx.stage.Stage;
import ubb.scs.map.controller.StageManager;
import ubb.scs.map.domain.entities.Notification;
import ubb.scs.map.domain.validators.*;
import ubb.scs.map.repository.NotificationRepository;
import ubb.scs.map.repository.database.*;
import ubb.scs.map.service.MySocialNetworkService;
import ubb.scs.map.domain.network.CommunityNetwork;

import javax.xml.validation.Validator;

public class MainApplication extends Application {
    private StageManager stageManager;

    @Override
    public void start(Stage primaryStage) {
        try {
            var uR = new UserDBRepository(new UserValidator(), "\"Users\"");
            var fR = new FriendshipDBRepository(new FriendshipValidator(), "\"Friendships\"");
            var fRR = new FriendshipRequestDBRepository(new FriendshipRequestValidator(), "\"FriendshipRequests\"");
            var mR = new MessageDBRepository(new MessageValidator(), "messages");


            var nR = new NotificationDBRepository( new NotificationValidator(), "notifications");
            var service = new MySocialNetworkService(uR, fR, new CommunityNetwork(), fRR, mR, nR);

            stageManager = new StageManager(primaryStage, service);

            stageManager.switchToLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}