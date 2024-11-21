package ubb.scs.map.repository.file;

import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.FriendshipRepository;

import java.util.Optional;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship>
        implements FriendshipRepository {
    private Long nextID;

    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Friendship createEntity(String line) {

        String[] splited = line.split(";");
        Friendship f = new Friendship(Long.parseLong(splited[1]),
                Long.parseLong(splited[2]));
        f.setId(Long.parseLong(splited[0]));
        if (nextID == null) nextID = f.getId();
        if (f.getId() > nextID) {
            nextID = f.getId() + 1;
        }
        return f;
    }

    @Override
    public String saveEntity(Friendship entity) {
        return entity.getId() + ";" + entity.getFirst() + ";" + entity.getSecond();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity.getId() != null) {
            entity.setId(nextID++);
        }
        var e = super.save(entity);
        writeToFile();
        return e;
    }

    @Override
    public Optional<Friendship> delete(Long aLong) {
        var e = super.delete(aLong);
        writeToFile();
        return e;
    }

    public Optional<Friendship> deleteFriendship(Long userId1, Long userID2) {
        var ID = findFriendship(userId1, userID2);
        var e = delete(ID);
        writeToFile();
        return e;
    }


    private Long findFriendship(Long userID1, Long userID2) {
        for (Friendship friendship : findAll()) {
            if ((friendship.getFirst().equals(userID1) && friendship.getSecond().equals(userID2)) ||
                    (friendship.getFirst().equals(userID2) && friendship.getSecond().equals(userID1))) {
                return friendship.getId();
            }
        }
        return null;
    }
}
