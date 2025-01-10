package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.Message;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.domain.exceptions.RepoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {
    public MessageDBRepository(Validator<Message> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected String getInsertParameters() {
        return "(?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateParameters() {
        return "message = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 1;
    }

    @Override
    protected Message createEntity(ResultSet data) {
        try {
            Long id = data.getLong("id");
            Long from = data.getLong("from");
            Long to = data.getLong("to");
            String message = data.getString("message");
            LocalDateTime sentAt = data.getTimestamp("sentAt").toLocalDateTime();
            Long messageId = data.getLong("messageid");

            Message msg = new Message(from, to, message, sentAt, messageId);
            msg.setId(id);
            return msg;
        } catch (SQLException e) {
            throw new RepoException("Error creating message: " + e.getMessage());
        }
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Message entity) {
        try {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getFrom());
            preparedStatement.setLong(3, entity.getTo());
            preparedStatement.setString(4, entity.getMessage());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getSentAt()));
            preparedStatement.setLong(6, entity.getMessageId());
        } catch (SQLException e) {
            throw new RepoException("Error saving message: " + e.getMessage());
        }
    }
}