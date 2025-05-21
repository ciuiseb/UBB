using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using cs.Domain;
using cs.Repository.Abstracts;
using cs.Repository.Interfaces;

namespace cs.Repository.Impl;

public class ResultRepositoryImpl : AbstractDBRepository<long, Result>, IResultRepository
{
    private readonly IEventRepository _eventRepository;
    private readonly IParticipantRepository _participantRepository;

    public ResultRepositoryImpl(IEventRepository eventRepository, IParticipantRepository participantRepository)
        : base("Results", "id")
    {
        _eventRepository = eventRepository;
        _participantRepository = participantRepository;
    }

    protected override string GetInsertParameters()
    {
        return "event_id, participant_id, discipline_id, points";
    }

    protected override string GetUpdateParameters()
    {
        return "event_id=@eventId, participant_id=@participantId, discipline_id=@disciplineId, points=@points";
    }

    protected override int GetUpdateParameterCount()
    {
        return 4;
    }

    protected override Result CreateEntity(IDataReader reader)
    {
        long id = Convert.ToInt64(reader["id"]);
        long eventId = Convert.ToInt64(reader["event_id"]);
        long participantId = Convert.ToInt64(reader["participant_id"]);
        int disciplineId = Convert.ToInt32(reader["discipline_id"]);
        int points = Convert.ToInt32(reader["points"]);

        Event event_ = _eventRepository.FindOne(eventId);
        Participant participant = _participantRepository.FindOne(participantId);

        Discipline discipline = disciplineId switch
        {
            1 => Discipline.RUNNING,
            2 => Discipline.SWIMMING,
            3 => Discipline.CYCLING,
            _ => throw new DataException($"Unknown discipline ID: {disciplineId}")
        };

        Result result = new Result(event_, participant, discipline, points);
        result.SetId(id);
        return result;
    }

    protected override void SetParameters(SQLiteCommand command, Result entity)
    {
        command.Parameters.AddWithValue("@eventId", entity.GetEvent().GetId());
        command.Parameters.AddWithValue("@participantId", entity.GetParticipant().GetId());

        int disciplineId = entity.GetDiscipline() switch
        {
            Discipline.RUNNING => 1,
            Discipline.SWIMMING => 2,
            Discipline.CYCLING => 3,
            _ => throw new ArgumentException($"Unknown discipline: {entity.GetDiscipline()}")
        };

        command.Parameters.AddWithValue("@disciplineId", disciplineId);
        command.Parameters.AddWithValue("@points", entity.GetPoints());
    }

    public List<Result> FindByEvent(int eventId)
    {
        string sql = "SELECT * FROM Results WHERE event_id = @eventId";
        List<Result> results = new List<Result>();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@eventId", eventId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                results.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            // Logger.LogError($"Error finding results by event ID: {ex.Message}");
        }

        return results;
    }

    public List<Result> FindByParticipant(int participantId)
    {
        string sql = "SELECT * FROM Results WHERE participant_id = @participantId";
        List<Result> results = new List<Result>();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@participantId", participantId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                results.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            // Logger.LogError($"Error finding results by participant ID: {ex.Message}");
        }

        return results;
    }
}