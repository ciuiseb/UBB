using System;
using System.Data.SQLite;
using Triathlon.Model;
using Triathlon.Persistence.Abstracts;
using Triathlon.Persistence.Interfaces;

namespace Triathlon.Persistence.Impl
{
    public class ParticipantRepositoryImpl : AbstractDbRepository<long, Participant>, IParticipantRepository
    {
        public ParticipantRepositoryImpl() : base("Participants", "id")
        {
        }

        protected override string GetInsertParameters()
        {
            return "@FirstName, @LastName, @Age, @Gender";
        }

        protected override string GetUpdateParameters()
        {
            return "first_name=@FirstName, last_name=@LastName, age=@Age, gender=@Gender";
        }

        protected override int GetUpdateParameterCount()
        {
            return 4;
        }

        protected override Participant CreateEntity(SQLiteDataReader data)
        {
            long id = data.GetInt64(data.GetOrdinal("id"));
            string firstName = data.GetString(data.GetOrdinal("first_name"));
            string lastName = data.GetString(data.GetOrdinal("last_name"));
            int age = data.GetInt32(data.GetOrdinal("age"));
            string gender = data.GetString(data.GetOrdinal("gender"));

            Participant participant = new Participant(firstName, lastName, age, gender);
            participant.Id = id;
            return participant;
        }

        protected override void SaveEntity(SQLiteCommand command, Participant entity)
        {
            command.Parameters.AddWithValue("@FirstName", entity.FirstName);
            command.Parameters.AddWithValue("@LastName", entity.LastName);
            command.Parameters.AddWithValue("@Age", entity.Age);
            command.Parameters.AddWithValue("@Gender", entity.Gender);
        }

        protected override long GetEntityId(Participant entity)
        {
            return entity.Id;
        }

        public Participant FindByName(string name)
        {
            string sql = "SELECT * FROM Participants WHERE first_name + ' ' + last_name = @Name";

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@Name", name);

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
                throw new Exception($"Error finding participant by name: {ex.Message}", ex);
            }
        }

        public Participant FindByEmail(string email)
        {
            string sql = "SELECT * FROM Participants WHERE email = @Email";

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@Email", email);

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
                throw new Exception($"Error finding participant by email: {ex.Message}", ex);
            }
        }
    }
}