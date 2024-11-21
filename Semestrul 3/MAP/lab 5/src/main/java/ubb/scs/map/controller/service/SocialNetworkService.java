package ubb.scs.map.controller.service;

import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ServiceException;

import java.util.ArrayList;

public interface SocialNetworkService<ID, U> {
    /**
     *
     * @param user -the user to be added
     *             must be not null
     * @return null - if the addition was succesful,
     *          the user if it's not
     * @throws ServiceException
     *                  if user is null.
     */
    public User addUser(U user);

    /**
     *
     * @param userId -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws ServiceException
     *                  if id is null.
     */
    public User getUser(ID userId);

    /**
     *
     * @param user -the user of the searched id
     *           user must not be null
     * @return the id
     *          null - if the user isnt registered
     * @throws ServiceException
     *                  if id is null.
     */
    public ID getUserID(U user);

    /**
     *
     *
     * @return all the users
     *
     */
    public Iterable<U> getAllUsers();
    /**
     *
     *
     * @return all the users
     *
     */
    public Iterable<Friendship> getAllFriendships();

    /**
     *
     * @param userId -the id of the entity to be deleted
     *           id must not be null
     * @return the id removed
     *          or null - if there is no entity with the given id
     * @throws ServiceException
     *                  if id is null.
     */
    public void deleteUser(ID userId);
    
    public void addFriendship(ID userId1, ID userId2);

    public void deleteFriendship(ID userId1, ID userId2);

    public ArrayList<ArrayList<ID>> getAllCommunities();

    public int getNumberOfCommunities();

    public ArrayList<Long> getMostSocialCommunity();
}
