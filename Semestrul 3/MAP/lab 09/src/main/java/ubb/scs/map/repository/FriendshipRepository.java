package ubb.scs.map.repository;

import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends Repository<Long, Friendship>{
    Optional<Friendship> deleteFriendship(Long userID1, Long userID2);
    List<User> getFriendsPage(Long userId, int pageNumber, int pageSize);

    int getTotalFriendsCount(Long userId);
}
