package ubb.scs.map.repository.file;

import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.network.CommunityNetwork;
import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.validators.Validator;

public class FriendshipRepository extends AbstractFileRepository<Long, Friendship> {
    private final CommunityNetwork communityNetwork;
    private Long nextID;
    public FriendshipRepository(Validator<Friendship> validator, String fileName, CommunityNetwork communityNetwork) {
        super(validator, fileName);
        this.communityNetwork = communityNetwork;
        setUpCommunity();
    }

    @Override
    public Friendship createEntity(String line) {

        String[] splited = line.split(";");
        Friendship f = new Friendship(Long.parseLong(splited[1]),
                                    Long.parseLong(splited[2]));
        f.setId(Long.parseLong(splited[0]));
        if(nextID == null) nextID = f.getId();
        if(f.getId() > nextID){
            nextID = f.getId() + 1;
        }
        return f;
    }

    @Override
    public String saveEntity(Friendship entity) {
        return entity.getId() + ";" + entity.getFirst() + ";" + entity.getSecond();
    }

    @Override
    public Friendship save(Friendship entity) {
        if(entity.getId() != null){
            entity.setId(nextID++);
        }
        var e = super.save(entity);

        if(communityNetwork != null) {
            try {
                communityNetwork.addEdge(entity.getFirst(), entity.getSecond());
            } catch (RepoException error) {
                throw new RepoException(error.getMessage());
            }
        }
        return e;
    }

    public Friendship deleteFriendship(Long userId1, Long userID2) {
        var ID = findFriendship(userId1, userID2);
        var e = delete(ID);
        try {
            communityNetwork.removeEdge(userId1, userID2);
        } catch (RepoException error) {
            throw new RepoException(error.getMessage());
        }
        return e;
    }

    public CommunityNetwork getCommunity() {
        return communityNetwork;
    }
    private void setUpCommunity(){
        for(var friendship: findAll()){
            try {
                communityNetwork.addEdge(friendship.getFirst(), friendship.getSecond());
            } catch (RepoException error) {
                throw new RepoException(error.getMessage());
            }
        }
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
