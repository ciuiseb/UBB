package triathlon.persistence.impl;

import triathlon.model.*;
import triathlon.persistence.interfaces.*;
import triathlon.persistence.abstracts.AbstractDBRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultRepositoryImpl extends AbstractDBRepository<Long, Result> implements ResultRepository {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    public ResultRepositoryImpl(EventRepository eventRepository, ParticipantRepository participantRepository) {
        super("Results", "id");
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    protected String getInsertParameters() {
        return "event_id, participant_id, discipline_id, points";
    }

    @Override
    protected String getUpdateParameters() {
        return "event_id=?, participant_id=?, discipline_id=?, points=?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 4;
    }

    @Override
    protected Result createEntity(ResultSet data) throws SQLException {
        Long id = data.getLong("id");
        Long eventId = data.getLong("event_id");
        Long participantId = data.getLong("participant_id");
        int disciplineId = data.getInt("discipline_id");
        int points = data.getInt("points");

        Event event = eventRepository.findOne(eventId).get();
        Participant participant = participantRepository.findOne(participantId).get();

        Discipline discipline = switch (disciplineId) {
            case 1 -> Discipline.RUNNING;
            case 2 -> Discipline.SWIMMING;
            case 3 -> Discipline.CYCLING;
            default -> throw new SQLException("Unknown discipline ID: " + disciplineId);
        };

        Result result = new Result(event, participant, discipline, points);
        result.setId(id);
        return result;
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Result entity) throws SQLException {
        preparedStatement.setLong(1, entity.getEvent().getId());
        preparedStatement.setLong(2, entity.getParticipant().getId());

        int disciplineId = switch (entity.getDiscipline()) {
            case RUNNING -> 1;
            case SWIMMING -> 2;
            case CYCLING -> 3;
        };

        preparedStatement.setInt(3, disciplineId);
        preparedStatement.setInt(4, entity.getPoints());
    }

    @Override
    public List<Result> findByEvent(Integer eventId) {
        String sql = "SELECT * FROM Results WHERE event_id = ?";
        List<Result> results = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
//            logger.error("Error finding results by event ID: " + e.getMessage());
        }

        return results;
    }

    @Override
    public List<Result> findByParticipant(Integer participantId) {
        String sql = "SELECT * FROM Results WHERE participant_id = ?";
        List<Result> results = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, participantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
//            logger.error("Error finding results by participant ID: " + e.getMessage());
        }

        return results;
    }
}