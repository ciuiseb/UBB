package controller.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ubb.scs.map.controller.service.MySocialNetworkService;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.domain.network.CommunityNetwork;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.file.FriendshipFileRepository;
import ubb.scs.map.repository.file.UserFileRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySocialNetworkServiceTest {

    private MySocialNetworkService socialNetworkService;
    private UserFileRepository userRepo;
    private FriendshipFileRepository friendshipRepo;

    @BeforeEach
    public void setUp() {
        CommunityNetwork communityNetwork = new CommunityNetwork();
        userRepo = new UserFileRepository(new UserValidator(),
                "src/test/resources/test_user_file.txt");
        friendshipRepo = new FriendshipFileRepository(new FriendshipValidator(),
                "src/test/resources/test_friendship_file.txt"
                );
        socialNetworkService = new MySocialNetworkService(userRepo, friendshipRepo, communityNetwork);
    }

    // Test adding a user successfully
    @Test
    public void addUserSuccessTest() {
        User user3 = new User("Alex", "c");
        User addedUser = socialNetworkService.addUser(user3);
        assertEquals(user3, addedUser, "The added user should match the user instance.");
    }

    // Test adding a null user
    @Test
    public void addUserNullTest() {
        assertThrows(ServiceException.class, () -> {
            socialNetworkService.addUser(null);
        }, "Adding a null user should throw a ServiceException.");
    }

    // Test retrieving a user by ID
    @Test
    public void getUserTest() {
        User retrievedUser = socialNetworkService.getUser(1L);
        assertEquals("IONUT", retrievedUser.getFirstName(), "The retrieved user should be IONUT.");
    }

    // Test retrieving a user that does not exist
    @Test
    public void getUserNotFoundTest() {
        assertNull(socialNetworkService.getUser(999L), "Retrieving a non-existing user should return null.");
    }

    // Test deleting a user
    @Test
    public void deleteUserTest() {
        socialNetworkService.deleteUser(1L);
        assertNull(socialNetworkService.getUser(1L), "The user should be deleted and not found.");
    }

    // Test deleting a non-existing user
    @Test
    public void deleteUserNotFoundTest() {
        assertThrows(ServiceException.class, () -> {
            socialNetworkService.deleteUser(999L);
        }, "Deleting a non-existing user should throw a ServiceException.");
    }

    // Test adding a friendship between existing users
    @Test
    public void addFriendshipTest() {
        User user3 = new User("Alex", "c");
        socialNetworkService.addUser(user3);
        socialNetworkService.addFriendship(2L, 3L);
        assertEquals(2, friendshipRepo.size(), "There should be 2 friendship.");
    }

    // Test deleting a friendship
    @Test
    public void deleteFriendshipTest() {
        socialNetworkService.deleteFriendship(1L, 2L);

        assertEquals(0, friendshipRepo.size(), "There should be 0 friendship.");
    }

    // Test retrieving all users
    @Test
    public void getAllUsersTest() {
        Iterable<User> allUsers = socialNetworkService.getAllUsers();
        assertNotNull(allUsers, "All users should not be null.");
        assertEquals(2, (((Collection<?>) allUsers).size()));
    }

    // Test retrieving communities
    @Test
    public void getAllCommunitiesTest() {
        ArrayList<ArrayList<Long>> communities = socialNetworkService.getAllCommunities();
        assertEquals(1, communities.size(), "There should be 1 community.");
        assertTrue(communities.get(0).contains(1L), "Community should contain user1 (IONUT).");
        assertTrue(communities.get(0).contains(2L), "Community should contain user2 (Mihai).");
    }

    @AfterEach
    public void tearDown() {
        try {
            Files.copy(Path.of("src/test/resources/test_user_file_backup.txt"),
                    Path.of("src/test/resources/test_user_file.txt"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of("src/test/resources/test_friendship_file_backup.txt"),
                    Path.of("src/test/resources/test_friendship_file.txt"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userRepo = null;
        friendshipRepo = null;
    }
}
