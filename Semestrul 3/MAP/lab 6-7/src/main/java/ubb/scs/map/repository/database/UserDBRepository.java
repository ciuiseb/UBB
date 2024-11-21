package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepository extends AbstractDBRepository<Long, User> implements UserRepository {
    public UserDBRepository(Validator<User> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected String getInsertParameters() {
        return "(?, ?, ?)";
    }

    @Override
    protected String getUpdateParameters() {
        return "firstName = ?, lastName = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 2;
    }

    @Override
    protected User createEntity(ResultSet data) {
        try {
            Long id = data.getLong("id");
            String firstName = data.getString("firstName");
            String lastName = data.getString("lastName");

            var u = new User(firstName, lastName);
            u.setId(id);

            return u;
        } catch (SQLException e) {
            throw new RepoException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, User entity) {
        try {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
        } catch (SQLException e) {
            throw new RepoException("Error saving user entity: " + e.getMessage());
        }
    }
}
