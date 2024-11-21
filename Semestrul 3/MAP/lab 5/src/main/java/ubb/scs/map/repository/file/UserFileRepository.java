package ubb.scs.map.repository.file;

import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.UserRepository;

import java.util.Optional;

public class UserFileRepository extends AbstractFileRepository<Long, User>
        implements UserRepository {
    private Long nextID;

    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public User createEntity(String line) {
        String[] splited = line.split(";");
        User u = new User(splited[1], splited[2]);
        u.setId(Long.parseLong(splited[0]));
        if(nextID == null) nextID = u.getId();
        if(u.getId() > nextID){
            nextID = u.getId() + 1;
        }
        return u;
    }

    @Override
    public String saveEntity(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }

    @Override
    public Optional<User> save(User entity) {
        if(entity != null && entity.getId() != null){
            entity.setId(nextID++);
        }
        var e = super.save(entity);
        writeToFile();
        return e;
    }

    @Override
    public Optional<User> update(User entity) {
        var e = super.update(entity);
        writeToFile();
        return e;
    }

    @Override
    public Optional<User> delete(Long aLong) {
        var e = super.delete(aLong);
        writeToFile();
        return e;
    }
}
