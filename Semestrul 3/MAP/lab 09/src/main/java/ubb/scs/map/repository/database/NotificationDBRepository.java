package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.Notification;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.NotificationRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NotificationDBRepository extends AbstractDBRepository<Long, Notification> implements NotificationRepository {

    public NotificationDBRepository(Validator<Notification> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    public Iterable<Notification> findAllForUser(Long userId) throws RepoException {
        String sql = String.format("SELECT * FROM %s WHERE user_id = ?", tableName);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return createList(resultSet);
        } catch (SQLException e) {
            throw new RepoException("Error finding notifications for user: " + e.getMessage());
        }
    }

    @Override
    protected String getInsertParameters() {
        return "(?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateParameters() {
        return "user_id = ?, type = ?, message = ?, date_time = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 4;
    }

    @Override
    protected Notification createEntity(ResultSet data) {
        try {
            Long id = data.getLong("id");
            Long userId = data.getLong("user_id");
            String type = data.getString("type");
            String message = data.getString("message");
            LocalDateTime dateTime = data.getObject("date_time", LocalDateTime.class);

            return new Notification(id, userId, type, message, dateTime);
        } catch (SQLException e) {
            throw new RepoException("Error creating notification: " + e.getMessage());
        }
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Notification entity) {
        try {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.setString(3, entity.getType());
            preparedStatement.setString(4, entity.getMessage());
            preparedStatement.setObject(5, entity.getDateTime());
        } catch (SQLException e) {
            throw new RepoException("Error saving notification entity: " + e.getMessage());
        }
    }
}