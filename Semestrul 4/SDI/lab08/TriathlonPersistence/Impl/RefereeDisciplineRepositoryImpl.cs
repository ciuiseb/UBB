using System;
using System.Collections.Generic;
using System.Data.SQLite;
using Triathlon.Model;
using Triathlon.Persistence.Abstracts;
using Triathlon.Persistence.Interfaces;

namespace Triathlon.Persistence.Impl
{
    public class RefereeDisciplineRepositoryImpl : AbstractDbRepository<long, RefereeDiscipline>, IRefereeDisciplineRepository
    {
        private readonly IRefereeRepository _refereeRepository;
        private readonly IEventRepository _eventRepository;

        public RefereeDisciplineRepositoryImpl(IRefereeRepository refereeRepository,
                                              IEventRepository eventRepository)
            : base("RefereeDisciplines", "id")
        {
            _refereeRepository = refereeRepository;
            _eventRepository = eventRepository;
        }

        protected override string GetInsertParameters()
        {
            return "@RefereeId, @EventId, @DisciplineId, @AssignedDate";
        }

        protected override string GetUpdateParameters()
        {
            return "referee_id=@RefereeId, event_id=@EventId, discipline_id=@DisciplineId, assigned_date=@AssignedDate";
        }

        protected override int GetUpdateParameterCount()
        {
            return 4;
        }

        protected override RefereeDiscipline CreateEntity(SQLiteDataReader data)
        {
            long id = data.GetInt64(data.GetOrdinal("id"));
            long refereeId = data.GetInt64(data.GetOrdinal("referee_id"));
            long eventId = data.GetInt64(data.GetOrdinal("event_id"));
            int disciplineId = data.GetInt32(data.GetOrdinal("discipline_id"));

            Referee referee = _refereeRepository.GetById(refereeId);
            Event @event = _eventRepository.GetById(eventId);
            Discipline discipline = disciplineId switch
            {
                1 => Discipline.RUNNING,
                2 => Discipline.SWIMMING,
                3 => Discipline.CYCLING,
                _ => throw new InvalidOperationException($"Unknown discipline ID: {disciplineId}")
            };

            return new RefereeDiscipline(id, referee, @event, discipline);
        }

        protected override void SaveEntity(SQLiteCommand command, RefereeDiscipline entity)
        {
            command.Parameters.AddWithValue("@RefereeId", entity.Referee.Id);
            command.Parameters.AddWithValue("@EventId", entity.Event.Id);
            command.Parameters.AddWithValue("@DisciplineId", (int)entity.Discipline);
            command.Parameters.AddWithValue("@AssignedDate", DateTime.Now);         }

        protected override long GetEntityId(RefereeDiscipline entity)
        {
            return entity.Id;
        }

        public RefereeDiscipline FindByRefereeId(long refereeId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE referee_id = @RefereeId";
            List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@RefereeId", refereeId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            refereeDisciplines.Add(CreateEntity(reader));
                        }
                    }
                }

                return refereeDisciplines.Count > 0 ? refereeDisciplines[0] : null;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding referee discipline by referee ID: {ex.Message}", ex);
            }
        }

        public IEnumerable<RefereeDiscipline> FindByEventId(long eventId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE event_id = @EventId";
            List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

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
                            refereeDisciplines.Add(CreateEntity(reader));
                        }
                    }
                }

                return refereeDisciplines;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding referee disciplines by event ID: {ex.Message}", ex);
            }
        }

        public IEnumerable<RefereeDiscipline> FindByDisciplineId(long disciplineId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE discipline_id = @DisciplineId";
            List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@DisciplineId", disciplineId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            refereeDisciplines.Add(CreateEntity(reader));
                        }
                    }
                }

                return refereeDisciplines;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding referee disciplines by discipline ID: {ex.Message}", ex);
            }
        }

        public IEnumerable<RefereeDiscipline> FindByRefereeAndEvent(long refereeId, long eventId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE referee_id = @RefereeId AND event_id = @EventId";
            List<RefereeDiscipline> refereeDisciplines = new List<RefereeDiscipline>();

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@RefereeId", refereeId);
                    command.Parameters.AddWithValue("@EventId", eventId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            refereeDisciplines.Add(CreateEntity(reader));
                        }
                    }
                }

                return refereeDisciplines;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding referee disciplines by referee and event ID: {ex.Message}", ex);
            }
        }

        public RefereeDiscipline FindByRefereeEventAndDiscipline(long refereeId, long eventId, long disciplineId)
        {
            string sql = $"SELECT * FROM {TableName} WHERE referee_id = @RefereeId AND event_id = @EventId AND discipline_id = @DisciplineId";

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@RefereeId", refereeId);
                    command.Parameters.AddWithValue("@EventId", eventId);
                    command.Parameters.AddWithValue("@DisciplineId", disciplineId);

                    using (SQLiteDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return CreateEntity(reader);
                        }
                    }
                }

                return null;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding referee discipline by referee, event, and discipline ID: {ex.Message}", ex);
            }
        }
    }
}