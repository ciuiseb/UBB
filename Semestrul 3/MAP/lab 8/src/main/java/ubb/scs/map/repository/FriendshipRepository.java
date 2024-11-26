package ubb.scs.map.repository;

import ubb.scs.map.domain.entities.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends Repository<Long, Friendship>{
    Optional<Friendship> deleteFriendship(Long userID1, Long userID2);
}
