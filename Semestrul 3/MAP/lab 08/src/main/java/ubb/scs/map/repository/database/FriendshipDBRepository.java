package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.FriendshipRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class FriendshipDBRepository extends AbstractDBRepository<Long, Friendship> implements FriendshipRepository {
    public FriendshipDBRepository(Validator<Friendship> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected String getInsertParameters() {
        return "(?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateParameters() {
        return "first = ?, second = ?, friends_since = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 3;
    }

    @Override
    protected Friendship createEntity(ResultSet data) {
        try {
            Long id = data.getLong("id");
            Long first = data.getLong("first");
            Long second = data.getLong("second");
            LocalDate date = data.getDate("friends_since").toLocalDate();
            var f = new Friendship(first, second, date);
            f.setId(id);
            return f;
        } catch (SQLException e) {
            throw new RepoException("Error creating friendship entity: " + e.getMessage());
        }
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Friendship entity) {
        try {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getFirst());
            preparedStatement.setLong(3, entity.getSecond());
            preparedStatement.setDate(4, Date.valueOf(entity.getFriendsSince()));
        } catch (SQLException e) {
            throw new RepoException("Error saving friendship entity: " + e.getMessage());
        }
    }

    @Override
    public Optional<Friendship> deleteFriendship(Long userID1, Long userID2) {
        String sql = String.format(
                "DELETE FROM %s WHERE (first = ? AND second = ?) OR (first = ? AND second = ?) RETURNING *",
                tableName
        );

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userID1);
            statement.setLong(2, userID2);
            statement.setLong(3, userID2);
            statement.setLong(4, userID1);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(createEntity(resultSet));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RepoException("Error deleting friendship: " + e.getMessage());
        }
    }
}