package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.FriendshipRequest;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;

import static ubb.scs.map.service.MySocialNetworkService.streamOf;

public class FriendshipRequestDBRepository extends AbstractDBRepository<Long, FriendshipRequest> {

    public FriendshipRequestDBRepository(Validator<FriendshipRequest> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected String getInsertParameters() {
        return "(?, ?, ?, ?)";  // id, first_user_id, second_user_id, sent_date
    }

    @Override
    protected String getUpdateParameters() {
        return "first_user_id = ?, second_user_id = ?, sent_date = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 3;
    }

    @Override
    protected FriendshipRequest createEntity(ResultSet data) {
        try {
            Long id = data.getLong("id");
            Long firstUserId = data.getLong("first_user_id");
            Long secondUserId = data.getLong("second_user_id");
            LocalDateTime sentDate = data.getTimestamp("sent_date").toLocalDateTime();

            FriendshipRequest request = new FriendshipRequest(firstUserId, secondUserId, sentDate);
            request.setId(id);

            return request;
        } catch (SQLException e) {
            throw new RepoException("Error creating friendship request entity: " + e.getMessage());
        }
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, FriendshipRequest entity) {
        try {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getFirst());
            preparedStatement.setLong(3, entity.getSecond());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(
                    entity.getSentDate() != null ? entity.getSentDate() : LocalDateTime.now()
            ));
        } catch (SQLException e) {
            throw new RepoException("Error saving friendship request entity: " + e.getMessage());
        }
    }

    public Iterable<FriendshipRequest> getRequestsForUser(Long userId) {
        String sql = String.format(
                "SELECT * FROM %s WHERE second_user_id = ?",
                tableName
        );

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            return createList(resultSet);

        } catch (SQLException e) {
            throw new RepoException("Error getting friendship requests: " + e.getMessage());
        }
    }

    public Long existsRequest(Long senderId, Long receiverId) {
        return streamOf(findAll())
                .filter(request -> (request.getFirst().equals(senderId) && request.getSecond().equals(receiverId)) ||
                        (request.getFirst().equals(receiverId) && request.getSecond().equals(senderId)))
                .map(FriendshipRequest::getId)
                .findFirst()
                .orElse(null);
    }
}