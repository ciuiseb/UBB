using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using cs.Domain;
using cs.Repository.Abstracts;
using cs.Repository.Interfaces;

namespace cs.Repository.Impl;

public class RefereeDisciplineRepositoryImpl : AbstractDBRepository<long, RefereeDiscipline>, IRefereeDisciplineRepository
{
    private readonly IRefereeRepository _refereeRepository;
    private readonly IEventRepository _eventRepository;

    public RefereeDisciplineRepositoryImpl(IRefereeRepository refereeRepository,
                                          IEventRepository eventRepository) : base("RefereeDisciplines", "id")
    {
        _refereeRepository = refereeRepository;
        _eventRepository = eventRepository;
    }

    protected override string GetInsertParameters()
    {
        return "referee_id, event_id, discipline_id, assigned_date";
    }

    protected override string GetUpdateParameters()
    {
        return "referee_id=@refereeId, event_id=@eventId, discipline_id=@disciplineId, assigned_date=@assignedDate";
    }

    protected override int GetUpdateParameterCount()
    {
        return 4;
    }

    protected override RefereeDiscipline CreateEntity(IDataReader reader)
    {
        long id = Convert.ToInt64(reader["id"]);
        long refereeId = Convert.ToInt64(reader["referee_id"]);
        long eventId = Convert.ToInt64(reader["event_id"]);
        int disciplineId = Convert.ToInt32(reader["discipline_id"]);

        Referee referee = _refereeRepository.FindOne(refereeId);
        Event event_ = _eventRepository.FindOne(eventId);
        Discipline discipline = disciplineId switch
        {
            1 => Discipline.RUNNING,
            2 => Discipline.SWIMMING,
            3 => Discipline.CYCLING,
            _ => throw new DataException($"Unknown discipline ID: {disciplineId}")
        };

        RefereeDiscipline refDiscipline = new RefereeDiscipline(id, referee, event_, discipline);

        return refDiscipline;
    }

    protected override void SetParameters(SQLiteCommand command, RefereeDiscipline entity)
    {
        command.Parameters.AddWithValue("@refereeId", entity.GetReferee().GetId());
        command.Parameters.AddWithValue("@eventId", entity.GetEvent().GetId());
        command.Parameters.AddWithValue("@disciplineId", (int)entity.GetDiscipline());
        command.Parameters.AddWithValue("@assignedDate", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
    }

    public List<RefereeDiscipline> FindByRefereeId(long refereeId)
    {
        string sql = $"SELECT * FROM {_tableName} WHERE referee_id = @refereeId";
        List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@refereeId", refereeId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                refereeDisciplines.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
        }

        return refereeDisciplines;
    }

    public List<RefereeDiscipline> FindByEventId(long eventId)
    {
        string sql = $"SELECT * FROM {_tableName} WHERE event_id = @eventId";
        List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@eventId", eventId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                refereeDisciplines.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
        }

        return refereeDisciplines;
    }

    public List<RefereeDiscipline> FindByDisciplineId(long disciplineId)
    {
        string sql = $"SELECT * FROM {_tableName} WHERE discipline_id = @disciplineId";
        List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@disciplineId", disciplineId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                refereeDisciplines.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
        }

        return refereeDisciplines;
    }

    public List<RefereeDiscipline> FindByRefereeAndEvent(long refereeId, long eventId)
    {
        string sql = $"SELECT * FROM {_tableName} WHERE referee_id = @refereeId AND event_id = @eventId";
        List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@refereeId", refereeId);
            command.Parameters.AddWithValue("@eventId", eventId);

            using var reader = command.ExecuteReader();
            while (reader.Read())
            {
                refereeDisciplines.Add(CreateEntity(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
        }

        return refereeDisciplines;
    }

    public RefereeDiscipline FindByRefereeEventAndDiscipline(long refereeId, long eventId, long disciplineId)
    {
        string sql = $"SELECT * FROM {_tableName} WHERE referee_id = @refereeId AND event_id = @eventId AND discipline_id = @disciplineId";

        try
        {
            using var command = new SQLiteCommand(sql, _connection);
            command.Parameters.AddWithValue("@refereeId", refereeId);
            command.Parameters.AddWithValue("@eventId", eventId);
            command.Parameters.AddWithValue("@disciplineId", disciplineId);

            using var reader = command.ExecuteReader();
            if (reader.Read())
            {
                return CreateEntity(reader);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.ToString());
        }

        return null;
    }
}