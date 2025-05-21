using System;
using System.Collections.Generic;
using System.Data.SQLite;
using Triathlon.Model;
using Triathlon.Persistence.Abstracts;
using Triathlon.Persistence.Interfaces;

namespace Triathlon.Persistence.Impl
{
    public class ResultRepositoryImpl : AbstractDbRepository<long, Result>, IResultRepository
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
            return "@EventId, @ParticipantId, @DisciplineId, @Points";
        }

        protected override string GetUpdateParameters()
        {
            return "event_id=@EventId, participant_id=@ParticipantId, discipline_id=@DisciplineId, points=@Points";
        }

        protected override int GetUpdateParameterCount()
        {
            return 4;
        }

        protected override Result CreateEntity(SQLiteDataReader data)
        {
            long id = data.GetInt64(data.GetOrdinal("id"));
            long eventId = data.GetInt64(data.GetOrdinal("event_id"));
            long participantId = data.GetInt64(data.GetOrdinal("participant_id"));
            int disciplineId = data.GetInt32(data.GetOrdinal("discipline_id"));
            int points = data.GetInt32(data.GetOrdinal("points"));

            Event @event = _eventRepository.GetById(eventId);
            Participant participant = _participantRepository.GetById(participantId);

            Discipline discipline = disciplineId switch
            {
                1 => Discipline.RUNNING,
                2 => Discipline.SWIMMING,
                3 => Discipline.CYCLING,
                _ => throw new InvalidOperationException($"Unknown discipline ID: {disciplineId}")
            };

            Result result = new Result(@event, participant, discipline, points);
            result.Id = id;
            return result;
        }

        protected override void SaveEntity(SQLiteCommand command, Result entity)
        {
            command.Parameters.AddWithValue("@EventId", entity.Event.Id);
            command.Parameters.AddWithValue("@ParticipantId", entity.Participant.Id);

            int disciplineId = entity.Discipline switch
            {
                Discipline.RUNNING => 1,
                Discipline.SWIMMING => 2,
                Discipline.CYCLING => 3,
                _ => throw new InvalidOperationException($"Unknown discipline: {entity.Discipline}")
            };

            command.Parameters.AddWithValue("@DisciplineId", disciplineId);
            command.Parameters.AddWithValue("@Points", entity.Points);
        }

        protected override long GetEntityId(Result entity)
        {
            return entity.Id;
        }

        public IEnumerable<Result> FindByEventId(long eventId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE event_id = @EventId";
            List<Result> results = new List<Result>();

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@EventId", eventId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            results.Add(CreateEntity(reader));
                        }
                    }
                }

                return results;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding results by event ID: {ex.Message}", ex);
            }
        }

        public IEnumerable<Result> FindByParticipantId(long participantId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE participant_id = @ParticipantId";
            List<Result> results = new List<Result>();

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@ParticipantId", participantId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            results.Add(CreateEntity(reader));
                        }
                    }
                }

                return results;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding results by participant ID: {ex.Message}", ex);
            }
        }

        public IEnumerable<Result> FindByParticipantAndEvent(long participantId, long eventId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE participant_id = @ParticipantId AND event_id = @EventId";
            List<Result> results = new List<Result>();

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@ParticipantId", participantId);
                    command.Parameters.AddWithValue("@EventId", eventId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            results.Add(CreateEntity(reader));
                        }
                    }
                }

                return results;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding results by participant and event ID: {ex.Message}", ex);
            }
        }
    }
}