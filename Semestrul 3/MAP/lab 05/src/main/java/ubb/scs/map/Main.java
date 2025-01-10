package ubb.scs.map;

import ubb.scs.map.controller.app.App;
import ubb.scs.map.domain.network.CommunityNetwork;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.database.FriendshipDBRepository;
import ubb.scs.map.repository.database.UserDBRepository;
import ubb.scs.map.repository.file.FriendshipFileRepository;
import ubb.scs.map.repository.file.UserFileRepository;
import ubb.scs.map.controller.service.MySocialNetworkService;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        var uV = new UserValidator();
//        var uR = new UserFileRepository(uV, Paths.get("resources", "users.txt").toString());
        var uR = new UserDBRepository(uV, "\"Users\"");

        var comm = new CommunityNetwork();

        var fV = new FriendshipValidator();
//        var fR = new FriendshipFileRepository(fV, Paths.get("resources", "friendships.txt").toString());
        var fR = new FriendshipDBRepository(fV, "\"Friendships\"");
        var service = new MySocialNetworkService(uR, fR, comm);
        var app = new App(service);

        app.run();
    }
}