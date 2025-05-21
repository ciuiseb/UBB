using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Data.SQLite;
using Triathlon.Model;
using Triathlon.Persistence.Abstracts;
using Triathlon.Persistence.Interfaces;

namespace Triathlon.Persistence.Impl
{
    public class EventRepositoryImpl : AbstractDbRepository<long, Event>, IEventRepository
    {
        public EventRepositoryImpl() : base("Events", "id")
        {
        }

        protected override string GetInsertParameters()
        {
            return "@Name, @Date, @Location, @Description";
        }

        protected override string GetUpdateParameters()
        {
            return "name=@Name, date=@Date, location=@Location, description=@Description";
        }

        protected override int GetUpdateParameterCount()
        {
            return 4;
        }

        protected override Event CreateEntity(SQLiteDataReader data)
        {
            long id = data.GetInt64(data.GetOrdinal("id"));
            string name = data.GetString(data.GetOrdinal("name"));
            string date = data.GetString(data.GetOrdinal("date"));
            string location = data.GetString(data.GetOrdinal("location"));
            string description = data.GetString(data.GetOrdinal("description"));

            return new Event(id, name, date, location, description);
        }

        protected override void SaveEntity(SQLiteCommand command, Event entity)
        {
            command.Parameters.AddWithValue("@Name", entity.Name);
            command.Parameters.AddWithValue("@Date", entity.Date);
            command.Parameters.AddWithValue("@Location", entity.Location);
            command.Parameters.AddWithValue("@Description", entity.Description);
        }

        protected override long GetEntityId(Event entity)
        {
            return entity.Id;
        }

        public IEnumerable<Event> FindByRefereeId(long refereeId)
        {
            string sql = "SELECT e.* FROM Events e " +
                         "JOIN Results r ON e.id = r.event_id " +
                         "WHERE r.referee_id = @RefereeId";

            List<Event> events = new List<Event>();

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
                            events.Add(CreateEntity(reader));
                        }
                    }
                }

                return events;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error finding events by referee ID: {ex.Message}", ex);
            }
        }
    }
}