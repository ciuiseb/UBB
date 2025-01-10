package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.entities.UserType;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.UserRepository;

import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepository extends AbstractDBRepository<Long, User> implements UserRepository {
    public UserDBRepository(Validator<User> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected String getInsertParameters() {
        return "(?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateParameters() {
        return "username = ?, first_name = ?, last_name = ?, encrypted_password = ?, user_type = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 5;
    }

    @Override
    protected User createEntity(ResultSet data) {
        try {
            Long id = data.getLong("id");
            String username = data.getString("username");
            String firstName = data.getString("first_name");
            String lastName = data.getString("last_name");
            String encryptedPassword = data.getString("encrypted_password");
            String userType = data.getString("user_type");

            var u = new User(username, firstName, lastName, encryptedPassword, userType);
            u.setId(id);


            return u;
        } catch (SQLException e) {
            throw new RepoException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, User entity) {
        try {
            preparedStatement.setString(
                    1,
                    entity.getUsername()
            );
            preparedStatement.setString(
                    2,
                    entity.getFirstName()
            );
            preparedStatement.setString(
                    3,
                    entity.getLastName()
            );
            preparedStatement.setString(
                    4,
                    encrypt(entity.getPassword())
            );
            preparedStatement.setString(5,
                    entity.getUserType().
                            toString().
                            toLowerCase()
            );
        } catch (SQLException e) {
            throw new RepoException("Error saving user entity: " + e.getMessage());
        } catch (Exception e) {
            throw new RepoException(e);
        }
    }

    @Override
    public String encrypt(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}