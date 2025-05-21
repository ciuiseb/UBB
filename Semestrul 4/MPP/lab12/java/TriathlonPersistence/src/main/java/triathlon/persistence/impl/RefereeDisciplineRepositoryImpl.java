package triathlon.persistence.impl;

import triathlon.model.*;
import triathlon.persistence.interfaces.*;
import triathlon.persistence.abstracts.AbstractDBRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RefereeDisciplineRepositoryImpl extends AbstractDBRepository<Long, RefereeDiscipline> implements RefereeDisciplineRepository {
    private final RefereeRepository refereeRepository;
    private final EventRepository eventRepository;

    public RefereeDisciplineRepositoryImpl(RefereeRepository refereeRepository,
                                           EventRepository eventRepository) {
        super("RefereeDisciplines", "id");
        this.refereeRepository = refereeRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    protected String getInsertParameters() {
        return "referee_id, event_id, discipline_id, assigned_date";
    }

    @Override
    protected String getUpdateParameters() {
        return "referee_id=?, event_id=?, discipline_id=?, assigned_date=?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 4;
    }

    @Override
    protected String getColumns() {
        return "";
    }

    @Override
    protected RefereeDiscipline createEntity(ResultSet data) throws SQLException {
        Long id = data.getLong("id");
        Long refereeId = data.getLong("referee_id");
        Long eventId = data.getLong("event_id");
        int disciplineId = data.getInt("discipline_id");

        Referee referee = refereeRepository.findOne(refereeId).orElse(null);
        Event event = eventRepository.findOne(eventId).orElse(null);
        Discipline discipline = switch (disciplineId) {
            case 1 -> Discipline.RUNNING;
            case 2 -> Discipline.SWIMMING;
            case 3 -> Discipline.CYCLING;
            default -> throw new SQLException("Unknown discipline ID: " + disciplineId);
        };

        RefereeDiscipline refDiscipline = new RefereeDiscipline(id, referee, event, discipline);

        return refDiscipline;
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, RefereeDiscipline entity) throws SQLException {
        preparedStatement.setLong(1, entity.getReferee().getId());
        preparedStatement.setLong(2, entity.getEvent().getId());
        preparedStatement.setInt(3, entity.getDiscipline().ordinal());
    }

    @Override
    public Optional<RefereeDiscipline> findByRefereeId(Long refereeId) {
        String sql = "SELECT * FROM " + tableName + " WHERE referee_id = ?";
        List<RefereeDiscipline> refereeDisciplines = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, refereeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    refereeDisciplines.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(refereeDisciplines.getFirst());
    }

    @Override
    public List<RefereeDiscipline> findByEventId(Long eventId) {
        String sql = "SELECT * FROM " + tableName + " WHERE event_id = ?";
        List<RefereeDiscipline> refereeDisciplines = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    refereeDisciplines.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return refereeDisciplines;
    }

    @Override
    public List<RefereeDiscipline> findByDisciplineId(Long disciplineId) {
        String sql = "SELECT * FROM " + tableName +" WHERE discipline_id = ?";
        List<RefereeDiscipline> refereeDisciplines = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, disciplineId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    refereeDisciplines.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return refereeDisciplines;
    }

    @Override
    public List<RefereeDiscipline> findByRefereeAndEvent(Long refereeId, Long eventId) {
        String sql = "SELECT * FROM " + tableName + " WHERE referee_id = ? AND event_id = ?";
        List<RefereeDiscipline> refereeDisciplines = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, refereeId);
            statement.setLong(2, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    refereeDisciplines.add(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return refereeDisciplines;
    }

    @Override
    public RefereeDiscipline findByRefereeEventAndDiscipline(Long refereeId, Long eventId, Long disciplineId) {
        String sql = "SELECT * FROM " + tableName + " WHERE referee_id = ? AND event_id = ? AND discipline_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, refereeId);
            statement.setLong(2, eventId);
            statement.setLong(3, disciplineId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}