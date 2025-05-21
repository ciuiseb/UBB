using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using cs.Domain;
using cs.Repository.Abstracts;
using cs.Repository.Interfaces;

namespace cs.Repository.Impl;

public class EventRepositoryImpl : AbstractDBRepository<long, Event>, IEventRepository
{
    public EventRepositoryImpl() : base("Events", "id")
    {
    }

    protected override string GetInsertParameters()
    {
        return "name, date, location, description";
    }

    protected override string GetUpdateParameters()
    {
        return "name=@name, date=@date, location=@location, description=@description";
    }

    protected override int GetUpdateParameterCount()
    {
        return 4;
    }

    protected override Event CreateEntity(IDataReader reader)
    {
        long id = Convert.ToInt64(reader["id"]);
        string name = reader["name"].ToString();
        string date = reader["date"].ToString();
        string location = reader["location"].ToString();
        string description = reader["description"].ToString();

        Event event_ = new(-1, name, date, location, description);
        event_.SetId(id);
        return event_;
    }

    protected override void SetParameters(SQLiteCommand command, Event entity)
    {
        command.Parameters.AddWithValue("@name", entity.GetName());
        command.Parameters.AddWithValue("@date", entity.GetDate());
        command.Parameters.AddWithValue("@location", entity.GetLocation());
        command.Parameters.AddWithValue("@description", entity.GetDescription());
    }

    public List<Event> FindByRefereeId(int refereeId)
    {
        string sql = "SELECT e.* FROM Events e " +
            "JOIN Results r ON e.id = r.event_id " +
            "WHERE r.referee_id = @refereeId";

        List<Event> events = new();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@refereeId", refereeId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                events.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            // Logger.LogError($"Error finding events by referee ID: {ex.Message}");
        }

        return events;
    }
}