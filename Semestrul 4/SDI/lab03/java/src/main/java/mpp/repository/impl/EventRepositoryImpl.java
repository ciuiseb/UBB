package mpp.repository.impl;

import mpp.domain.Event;
import mpp.repository.abstracts.AbstractDBRepository;
import mpp.repository.interfaces.EventRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventRepositoryImpl extends AbstractDBRepository<Long, Event> implements EventRepository {
    public EventRepositoryImpl() {
        super("Events", "id");
    }

    @Override
    protected String getInsertParameters() {
        return "name, date, location, description";
    }

    @Override
    protected String getUpdateParameters() {
        return "name=?, date=?, location=?, description=?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 4;
    }

    @Override
    protected Event createEntity(ResultSet data) throws SQLException {
        Long id = data.getLong("id");
        String name = data.getString("name");
        String date = data.getString("date");
        String location = data.getString("location");
        String description = data.getString("description");

        Event event = new Event(null, name, date, location, description);
        event.setId(id);
        return event;
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Event entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDate());
        preparedStatement.setString(3, entity.getLocation());
        preparedStatement.setString(4, entity.getDescription());
    }

    @Override
    public List<Event> findByRefereeId(Integer refereeId) {
        String sql = "SELECT e.* FROM Events e " +
                "JOIN Results r ON e.id = r.event_id " +
                "WHERE r.referee_id = ?";

        List<Event> events = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, refereeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    events.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
//            logger.error("Error finding events by referee ID: " + e.getMessage());
        }

        return events;
    }
}