package ubb.scs.map.service;


import ubb.scs.map.domain.entities.*;
import ubb.scs.map.domain.exceptions.DomainException;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.domain.network.CommunityNetwork;
import ubb.scs.map.repository.FriendshipRepository;
import ubb.scs.map.repository.UserRepository;
import ubb.scs.map.repository.database.FriendshipRequestDBRepository;
import ubb.scs.map.repository.database.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.lang.Iterable;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

public class MySocialNetworkService implements SocialNetworkService<Long, User> {
    private final UserRepository userRepo;
    private final FriendshipRepository friendshipRepo;
    private final CommunityNetwork communityNetwork;
    private final FriendshipRequestDBRepository friendshipRequestRepo;
    private final MessageDBRepository messageRepo;
    private User currentUser = null;

    public MySocialNetworkService(UserRepository userRepo,
                                  FriendshipRepository friendshipRepo,
                                  CommunityNetwork communityNetwork,
                                  FriendshipRequestDBRepository friendshipRequestRepo, MessageDBRepository messageRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.communityNetwork = communityNetwork;
        this.friendshipRequestRepo = friendshipRequestRepo;
        this.messageRepo = messageRepo;

        setUpCommunity();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param user -the user to be added
     *             must be not null
     * @return null - if the addition was succesful,
     * the user if it's not
     * @throws ServiceException if user is null.
     */
    @Override
    public User addUser(User user) {
        try {
            var max = streamOf(userRepo.findAll())
                    .map(User::getId)
                    .max(Long::compareTo)
                    .orElse(1L);
            user.setId(max + 1);
            return userRepo.save(user).get();
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param userId -the id of the entity to be returned
     *               id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws ServiceException if id is null.
     */
    @Override
    public User getUser(Long userId) {
        if (userRepo.findOne(userId).isPresent())
            return userRepo.findOne(userId).get();
        return null;
    }

    /**
     * @param user -the user of the searched id
     *             user must not be null
     * @return the id
     * null - if the user isn't registered
     * @throws ServiceException if id is null.
     */
    @Override
    public Long getUserID(User user) {
        return streamOf(userRepo.findAll())
                .filter(u -> u.getFirstName().equals(user.getFirstName())
                        && u.getLastName().equals(user.getLastName()))
                .findFirst()
                .map(User::getId)
                .orElseThrow(() -> new ServiceException("User doesnt exist"));
    }

    /**
     * @return all the users
     */
    @Override
    public Iterable<User> getAllUsers() {
        try {
            return userRepo.findAll();
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Iterable<Friendship> getAllFriendships() {
        try {
            return friendshipRepo.findAll();
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param userId -the id of the entity to be deleted
     *               id must not be null
     * @return the id removed
     * or null - if there is no entity with the given id
     * @throws ServiceException if id is null.
     */
    @Override
    public void deleteUser(Long userId) {
        try {
            userRepo.delete(userId);
            streamOf(friendshipRepo.findAll())
                    .filter(f -> (f.getFirst().equals(userId)
                            || f.getSecond().equals(userId)))
                    .toList()
                    .forEach(f -> deleteFriendship(f.getFirst(), f.getSecond()));
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void banUser(Long userId) {
        userRepo.findOne(userId).ifPresent(user -> {
            user.setUserType(UserType.BANNED);
            userRepo.update(user);
        });
    }

    public void unBanUser(Long userId) {
        userRepo.findOne(userId).ifPresent(user -> {
            user.setUserType(UserType.USER);
            userRepo.update(user);
        });
    }

    /**
     * @param userId1 -the id of the first entity
     * @param userId2 -the id of the second entity
     *                id must not be null
     * @throws ServiceException if id is null.
     */
    @Override
    public void addFriendship(Long userId1, Long userId2) {
        try {
            long max = streamOf(getAllFriendships())
                    .map(Friendship::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
            var f = new Friendship(userId1, userId2);
            f.setId(max + 1L);
            var e = friendshipRepo.save(f);
            e.ifPresent(friendship -> communityNetwork.addEdge(friendship.getFirst(), friendship.getSecond()));
        } catch (RepoException | DomainException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param userId1 -the id of the first entity
     * @param userId2 -the id of the second entity
     *                id must not be null
     * @throws ServiceException if id is null.
     */
    @Override
    public void deleteFriendship(Long userId1, Long userId2) {
        try {
            friendshipRepo.deleteFriendship(userId1, userId2);
            communityNetwork.removeEdge(userId1, userId2);
        } catch (RepoException | DomainException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addFriendshipRequest(Long userId1, Long userId2) {
        try {
            if (friendshipRequestRepo.existsRequest(userId1, userId2) != null
                    || friendshipRequestRepo.existsRequest(userId2, userId1) != null) {
                throw new RepoException("Firnedsip request already sent from one side!");
            }
            var id = streamOf(friendshipRequestRepo.findAll())
                    .map(FriendshipRequest::getId)
                    .max(Long::compareTo)
                    .orElse(0L)
                    + 1;
            friendshipRequestRepo.save(new FriendshipRequest(id, userId1, userId2, LocalDateTime.now()));
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteFriendshipRequest(Long friendshipRequestID) {
        try {
            friendshipRequestRepo.delete(friendshipRequestID);
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return all the communities
     */
    @Override
    public ArrayList<ArrayList<Long>> getAllCommunities() {
        try {
            return communityNetwork.getCommunities();
        } catch (DomainException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return the number of communities
     */
    @Override
    public int getNumberOfCommunities() {
        try {
            return communityNetwork.getNumberOfCommunities();
        } catch (DomainException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return the most social community
     */
    @Override
    public ArrayList<Long> getMostSocialCommunity() {
        try {
            return communityNetwork.getMostSocialGroup();
        } catch (DomainException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Optional<User> logIn(String userName, String password) {
        if (userName == null || password == null ||
                userName.trim().isEmpty() || password.trim().isEmpty()) {
            throw new ServiceException("Username and password cannot be empty");
        }

        try {
            String encryptedPassword = userRepo.encrypt(password);
            Optional<User> user = streamOf(getAllUsers())
                    .filter(u -> u.getUsername().equals(userName.trim()))
                    .filter(u -> u.getPassword().equals(encryptedPassword))
                    .findFirst();

            user.ifPresent(u -> this.currentUser = u);
            return user;
        } catch (Exception e) {
            throw new ServiceException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public void logout() {
        if (currentUser == null) {
            throw new ServiceException("No user is currently logged in");
        }
        currentUser = null;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            var max = streamOf(messageRepo.findAll())
                    .map(Message::getId)
                    .max(Long::compareTo)
                    .orElse(1L);
            message.setId(max + 1);
            messageRepo.save(message);
        } catch (RepoException e) {
            throw new ServiceException("Error sending message! : " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(Long messageId) {
        try {
            messageRepo.delete(messageId);
        } catch (RepoException e) {
            throw new ServiceException("Error unseding the message!");
        }
    }

    @Override
    public void updateMessage(Message message) {
        try {
            messageRepo.update(message);
        } catch (RepoException e) {
            throw new ServiceException("Error editing the message!");
        }
    }

    public static <T> Stream<T> streamOf(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private void setUpCommunity() throws ServiceException {
        try {
            streamOf(friendshipRepo.findAll())
                    .forEach(f -> communityNetwork.addEdge(f.getFirst(), f.getSecond()));
        } catch (RepoException | DomainException error) {
            throw new ServiceException(error.getMessage());
        }
    }

    public Iterable<FriendshipRequest> getFriendshipRequests(User user) {
        var userID = user.getId();
        return friendshipRequestRepo.getRequestsForUser(userID);
    }

    public void acceptFriendshipRequest(FriendshipRequest fr) {
        try {
            addFriendship(fr.getFirst(), fr.getSecond());
            friendshipRequestRepo.delete(fr.getId());
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void denyFriendshipRequest(FriendshipRequest fr) {
        try {
            friendshipRequestRepo.delete(fr.getId());
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Message> getMessages(Long from, Long to) {
        return StreamSupport.stream(messageRepo.findAll().spliterator(), false)
                .filter(message ->
                        (message.getFrom().equals(from) && message.getTo().equals(to)) ||
                                (message.getFrom().equals(to) && message.getTo().equals(from)))
                .sorted(Comparator.comparing(Message::getSentAt))
                .toList();
    }
    public Message getMessage(Long messageId) {
        try {
            Optional<Message> message = messageRepo.findOne(messageId);
            return message.orElse(null);
        } catch (RepoException e) {
            throw new ServiceException("Error getting message: " + e.getMessage());
        }
    }
}