package mpp.repository.impl;

import mpp.domain.Referee;
import mpp.repository.abstracts.AbstractDBRepository;
import mpp.repository.interfaces.RefereeRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RefereeRepositoryImpl extends AbstractDBRepository<Long, Referee> implements RefereeRepository {
    public RefereeRepositoryImpl() {
        super("Referees", "id");
    }

    @Override
    protected String getInsertParameters() {
        return "username, password, email, first_name, last_name";
    }

    @Override
    protected String getUpdateParameters() {
        return "username=?, password=?, email=?, first_name=?, last_name=?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 5;
    }

    @Override
    protected Referee createEntity(ResultSet data) throws SQLException {
        Long id = data.getLong("id");
        String username = data.getString("username");
        String password = data.getString("password");
        String email = data.getString("email");
        String firstName = data.getString("first_name");
        String lastName = data.getString("last_name");

        Referee referee = new Referee(username, password, email, firstName, lastName);
        referee.setId(id);
        return referee;
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Referee entity) throws SQLException {
        preparedStatement.setString(1, entity.getUsername());
        preparedStatement.setString(2, entity.getPassword());
        preparedStatement.setString(3, entity.getEmail());
        preparedStatement.setString(4, entity.getFirstName());
        preparedStatement.setString(5, entity.getLastName());
    }

    @Override
    public Optional<Referee> findByUsername(String username) {
        String sql = "SELECT * FROM " + tableName + " WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
//            logger.error("Error finding referee by username: " + e.getMessage());
        }

        return Optional.empty();
    }
}