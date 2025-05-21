using System;
using System.Data;
using System.Data.SQLite;
using cs.Domain;
using cs.Repository.Abstracts;
using cs.Repository.Interfaces;

namespace cs.Repository.Impl;

public class ParticipantRepositoryImpl : AbstractDBRepository<long, Participant>, IParticipantRepository
{
    public ParticipantRepositoryImpl() : base("Participants", "id")
    {
    }

    protected override string GetInsertParameters()
    {
        return "first_name, last_name, age, gender";
    }

    protected override string GetUpdateParameters()
    {
        return "first_name=@firstName, last_name=@lastName, age=@age, gender=@gender";
    }

    protected override int GetUpdateParameterCount()
    {
        return 4;
    }

    protected override Participant CreateEntity(IDataReader reader)
    {
        long id = Convert.ToInt64(reader["id"]);
        string firstName = reader["first_name"].ToString();
        string lastName = reader["last_name"].ToString();
        int age = Convert.ToInt32(reader["age"]);
        string gender = reader["gender"].ToString();

        Participant participant = new Participant(firstName, lastName, age, gender);
        participant.SetId(id);
        return participant;
    }

    protected override void SetParameters(SQLiteCommand command, Participant entity)
    {
        command.Parameters.AddWithValue("@firstName", entity.GetFirstName());
        command.Parameters.AddWithValue("@lastName", entity.GetLastName());
        command.Parameters.AddWithValue("@age", entity.GetAge());
        command.Parameters.AddWithValue("@gender", entity.GetGender());
    }

    public Participant FindByName(string name)
    {
        string sql = "SELECT * FROM Participants WHERE first_name || ' ' || last_name = @name";

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@name", name);

            using var reader = command.ExecuteReader();
            if (reader.Read())
            {
                return CreateEntity(reader);
            }
        }
        catch (Exception ex)
        {
            // Logger.LogError($"Error finding participant by name: {ex.Message}");
        }

        return null;
    }

    public Participant FindByEmail(string email)
    {
        string sql = "SELECT * FROM Participants WHERE email = @email";

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@email", email);

            using var reader = command.ExecuteReader();
            if (reader.Read())
            {
                return CreateEntity(reader);
            }
        }
        catch (Exception ex)
        {
            // Logger.LogError($"Error finding participant by email: {ex.Message}");
        }

        return null;
    }
}