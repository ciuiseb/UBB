package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.FriendshipRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public List<User> getFriendsPage(Long userId, int pageNumber, int pageSize) {
        List<User> friends = new ArrayList<>();
        String sql = String.format("""
                SELECT DISTINCT u.* FROM "Users" u
                JOIN %s f ON (f.first = u.id OR f.second = u.id)
                WHERE (f.first = ? OR f.second = ?)
                AND u.id != ?
                AND u.user_type = 'user'
                ORDER BY u.id
                LIMIT ? OFFSET ?
                """, tableName);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.setLong(3, userId);
            statement.setInt(4, pageSize);
            statement.setInt(5, pageNumber * pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("encrypted_password"),
                        resultSet.getString("user_type")
                );
                user.setId(resultSet.getLong("id"));
                friends.add(user);
            }
            return friends;
        } catch (SQLException e) {
            throw new RepoException("Error getting friends page: " + e.getMessage());
        }
    }

    public int getTotalFriendsCount(Long userId) {
        String sql = String.format("""
                SELECT COUNT(DISTINCT u.id) as count FROM "Users" u
                JOIN %s f ON (f.first = u.id OR f.second = u.id)
                WHERE (f.first = ? OR f.second = ?)
                AND u.id != ?
                AND u.user_type = 'user'
                """, tableName);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.setLong(3, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            throw new RepoException("Error getting total friends count: " + e.getMessage());
        }
    }

}