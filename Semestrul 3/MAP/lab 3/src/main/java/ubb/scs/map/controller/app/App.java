package ubb.scs.map.controller.app;

import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.controller.service.SocialNetworkService;

import java.util.Scanner;

public class App implements AbstractApp {
    private final SocialNetworkService<Long, User> service;
    private final Scanner scanner = new Scanner(System.in);

    public App(SocialNetworkService<Long, User> service) {
        this.service = service;
    }

    @Override
    public void printMenu() {
        System.out.println("Chose an option:");
        System.out.println("1. Add user");
        System.out.println("2. Add frienship");
        System.out.println("3. See users");
        System.out.println("4. Remove user");
        System.out.println("5. Remove friendship");
        System.out.println("6. See number of communities");
        System.out.println("7. See the most social community");
        System.out.println("");
        System.out.println("0. Exit");

    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int option;
            option = scanner.nextInt();
            switch (option) {
                case 1 -> addUser();
                case 2 -> addFriendship();
                case 3 -> seeUsers();
                case 4 -> removeUser();
                case 5 -> removeFriendship();
                case 6 -> seeNumberOfCommunities();
                case 7 -> seeMostSocialCommunity();
                case 0 -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid option!");
                }
            }
        }
    }

    private void addUser() {
        String firstName, lastName;
        System.out.println("First name: ");
        firstName = scanner.next();
        System.out.println("Last name: ");
        lastName = scanner.next();

        try {
            service.addUser(new User(firstName, lastName));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());

        }
    }

    private void addFriendship() {
        String firstName1, lastName1, firstName2, lastName2;
        System.out.println("First name of the first user: ");
        firstName1 = scanner.next();
        System.out.println("Last name of the first user: ");
        lastName1 = scanner.next();

        System.out.println("First name of the second user: ");
        firstName2 = scanner.next();
        System.out.println("Last name of the second user: ");
        lastName2 = scanner.next();

        try {
            service.addFriendship(
                    service.getUserID(new User(firstName1, lastName1)),
                    service.getUserID(new User(firstName2, lastName2))
            );

        } catch (ServiceException e) {
            System.out.println(e.getMessage());

        }
    }

    private void seeUsers() {
        for(var user: service.getAllUsers()){
            System.out.println(user);
        }
    }


    private void removeUser() {
        String firstName, lastName;
        System.out.println("First name: ");
        firstName = scanner.next();
        System.out.println("Last name: ");
        lastName = scanner.next();

        try{
            service.deleteUser(
                    service.getUserID(new User(firstName, lastName))
            );
        }catch (ServiceException e){
            System.out.println(e.getMessage());

        }
    }

    private void removeFriendship() {
        String firstName1, lastName1, firstName2, lastName2;
        System.out.println("First name of the first user: ");
        firstName1 = scanner.next();
        System.out.println("Last name of the first user: ");
        lastName1 = scanner.next();

        System.out.println("First name of the second user: ");
        firstName2 = scanner.next();
        System.out.println("Last name of the second user: ");
        lastName2 = scanner.next();

        try {
            service.deleteFriendship(
                    service.getUserID(new User(firstName1, lastName1)),
                    service.getUserID(new User(firstName2, lastName2))
            );
        } catch (ServiceException e) {
            System.out.println(e.getMessage());

        }
    }

    private void seeNumberOfCommunities() {
        System.out.println("There are " + service.getNumberOfCommunities() + " communities!");
    }

    private void seeMostSocialCommunity() {
        var community = service.getMostSocialCommunity();
        System.out.println("The most social community is made of:");
        for(var userID: community){
            if(userID != null)
                System.out.println(service.getUser(userID));
        }
    }
}
