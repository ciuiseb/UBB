package ubb.scs.map;

import javafx.application.Application;
import javafx.stage.Stage;
import ubb.scs.map.controller.gui.StageManager;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.validators.FriendshipRequestValidator;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.database.FriendshipDBRepository;
import ubb.scs.map.repository.database.FriendshipRequestDBRepository;
import ubb.scs.map.repository.database.UserDBRepository;
import ubb.scs.map.service.MySocialNetworkService;
import ubb.scs.map.domain.network.CommunityNetwork;

public class MainApplication extends Application {
    private StageManager stageManager;

    @Override
    public void start(Stage primaryStage) {
        try {
            var uV = new UserValidator();
            var uR = new UserDBRepository(uV, "\"Users\"");

            var comm = new CommunityNetwork();

            var fV = new FriendshipValidator();
            var fR = new FriendshipDBRepository(fV, "\"Friendships\"");

            var fRV = new FriendshipRequestValidator();
            var fRR = new FriendshipRequestDBRepository(fRV, "\"FriendshipRequests\"");
            var service = new MySocialNetworkService(uR, fR, comm, fRR);

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
