package ubb.scs.map.controller.app;

import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.controller.service.SocialNetworkService;

import java.util.InputMismatchException;
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
        System.out.println("4. See friendships");
        System.out.println("5. Remove user");
        System.out.println("6. Remove friendship");
        System.out.println("7. See number of communities");
        System.out.println("8. See the most social community");
        System.out.println("0. Exit");

    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int option;
            try{
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> addUser();
                    case 2 -> addFriendship();
                    case 3 -> seeUsers();
                    case 4 -> seeFriendships();
                    case 5 -> removeUser();
                    case 6 -> removeFriendship();
                    case 7 -> seeNumberOfCommunities();
                    case 8 -> seeMostSocialCommunity();
                    case 0 -> {
                        return;
                    }
                    default -> {
                        System.out.println("Invalid option!");
                    }
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextInt();
            }catch(ServiceException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addUser() {
        String firstName, lastName;
        long id;
        System.out.println("ID: ");
        id = scanner.nextLong();
        System.out.println("First name: ");
        firstName = scanner.next();
        System.out.println("Last name: ");
        lastName = scanner.next();

        service.addUser(new User(id, firstName, lastName));
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

        service.addFriendship(
                service.getUserID(new User(firstName1, lastName1)),
                service.getUserID(new User(firstName2, lastName2))
        );
    }

    private void seeUsers() {
        service.getAllUsers()
                .forEach(System.out::println);
    }
    private void seeFriendships(){
        service.getAllFriendships()
                .forEach(System.out::println);
    }

    private void removeUser() {
        String firstName, lastName;
        System.out.println("First name: ");
        firstName = scanner.next();
        System.out.println("Last name: ");
        lastName = scanner.next();

        service.deleteUser(
                service.getUserID(new User(firstName, lastName))
        );
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

        service.deleteFriendship(
                service.getUserID(new User(firstName1, lastName1)),
                service.getUserID(new User(firstName2, lastName2))
        );
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
