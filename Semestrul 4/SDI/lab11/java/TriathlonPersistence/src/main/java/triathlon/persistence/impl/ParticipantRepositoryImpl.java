package triathlon.persistence.impl;

import triathlon.model.Participant;
import triathlon.persistence.interfaces.*;
import triathlon.persistence.abstracts.AbstractDBRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantRepositoryImpl extends AbstractDBRepository<Long, Participant> implements ParticipantRepository {

    public ParticipantRepositoryImpl() {
        super("Participants", "id");
    }

    @Override
    protected String getInsertParameters() {
        return "first_name, last_name, age, gender";
    }

    @Override
    protected String getUpdateParameters() {
        return "first_name=?, last_name=?, age=?, gender=?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 4;
    }

    @Override
    protected Participant createEntity(ResultSet data) throws SQLException {
        Long id = data.getLong("id");
        String firstName = data.getString("first_name");
        String lastName = data.getString("last_name");
        int age = data.getInt("age");
        String gender = data.getString("gender");

        Participant participant = new Participant(firstName, lastName, age, gender);
        participant.setId(id);
        return participant;
    }

    @Override
    protected void saveEntity(PreparedStatement preparedStatement, Participant entity) throws SQLException {
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setInt(3, entity.getAge());
        preparedStatement.setString(4, entity.getGender());
    }

}