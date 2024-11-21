package ubb.scs.map.controller.service;


import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.FriendshipRepository;
import ubb.scs.map.repository.file.UserRepository;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Iterable;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

public class MySocialNetworkService implements SocialNetworkService<Long, User> {
    private final UserRepository userRepo;
    private final FriendshipRepository friendshipRepo;

//    private final Repository<Long, User> userRepo;
//    private final Repository<Long, Friendship> friendshipRepo;
    public MySocialNetworkService(UserRepository userRepo, FriendshipRepository friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;

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
            userRepo.save(user);
            userRepo.writeToFile();
            return user;
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
        return null; //
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
        return userRepo.findAll();
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
            userRepo.writeToFile();
            streamOf(friendshipRepo.findAll())
                    .filter(f -> (f.getFirst().equals(userId)
                            || f.getSecond().equals(userId)))
                    .toList()
                    .forEach(f -> deleteFriendship(f.getFirst(), f.getSecond()));
        } catch (RepoException e) {
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
    public void addFriendship(Long userId1, Long userId2) {
        try {
            friendshipRepo.save(new Friendship(userId1, userId2));
            friendshipRepo.writeToFile();
        } catch (RepoException e) {
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
            friendshipRepo.writeToFile();
        } catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return all the communities
     */
    @Override
    public ArrayList<ArrayList<Long>> getAllCommunities() {
        return friendshipRepo.getCommunity().getCommunities();
    }

    /**
     * @return the number of communities
     */
    @Override
    public int getNumberOfCommunities() {
        return friendshipRepo.getCommunity().getNumberOfCommunities();
    }

    /**
     * @return the most social community
     */
    @Override
    public ArrayList<Long> getMostSocialCommunity() {
        return friendshipRepo.getCommunity().getMostSocialGroup();
    }

    public static <T> Stream<T> streamOf(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
