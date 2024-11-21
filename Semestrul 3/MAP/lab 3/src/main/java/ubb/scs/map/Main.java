package ubb.scs.map;

import ubb.scs.map.controller.app.App;
import ubb.scs.map.domain.network.CommunityNetwork;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.file.FriendshipRepository;
import ubb.scs.map.repository.file.UserRepository;
import ubb.scs.map.controller.service.MySocialNetworkService;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        var uV = new UserValidator();
        var uR = new UserRepository(uV, Paths.get("data", "users.txt").toString());

        var comm = new CommunityNetwork();

        var fV = new FriendshipValidator();
        var fR = new FriendshipRepository(fV, Paths.get("data", "friendships.txt").toString(), comm);

        var service = new MySocialNetworkService(uR, fR);
        var app = new App(service);

        app.run();
    }
}