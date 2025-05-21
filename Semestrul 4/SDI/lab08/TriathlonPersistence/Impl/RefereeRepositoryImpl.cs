using System;
using System.Data.SQLite;
using Triathlon.Model;
using Triathlon.Persistence.Abstracts;
using Triathlon.Persistence.Interfaces;

namespace Triathlon.Persistence.Impl
{
    public class RefereeRepositoryImpl : AbstractDbRepository<long, Referee>, IRefereeRepository
    {
        public RefereeRepositoryImpl() : base("Referees", "id")
        {
        }

        protected override string GetInsertParameters()
        {
            return "@Username, @Password, @Email, @FirstName, @LastName";
        }

        protected override string GetUpdateParameters()
        {
            return "username=@Username, password=@Password, email=@Email, first_name=@FirstName, last_name=@LastName";
        }

        protected override int GetUpdateParameterCount()
        {
            return 5;
        }

        protected override Referee CreateEntity(SQLiteDataReader data)
        {
            long id = data.GetInt64(data.GetOrdinal("id"));
            string username = data.GetString(data.GetOrdinal("username"));
            string password = data.GetString(data.GetOrdinal("password"));
            string email = data.GetString(data.GetOrdinal("email"));
            string firstName = data.GetString(data.GetOrdinal("first_name"));
            string lastName = data.GetString(data.GetOrdinal("last_name"));

            Referee referee = new Referee(username, password, email, firstName, lastName);
            referee.Id = id;
            return referee;
        }

        protected override void SaveEntity(SQLiteCommand command, Referee entity)
        {
            command.Parameters.AddWithValue("@Username", entity.Username);
            command.Parameters.AddWithValue("@Password", entity.Password);
            command.Parameters.AddWithValue("@Email", entity.Email);
            command.Parameters.AddWithValue("@FirstName", entity.FirstName);
            command.Parameters.AddWithValue("@LastName", entity.LastName);
        }

        protected override long GetEntityId(Referee entity)
        {
            return entity.Id;
        }

        public Referee FindByUsername(string username)
        {
            string sql = $"SELECT * FROM {TableName} WHERE username = @Username";

            try
            {
                using (SQLiteCommand command = new SQLiteCommand(sql, Connection))
                {
                    if (Connection.State != System.Data.ConnectionState.Open)
                        Connection.Open();

                    command.Parameters.AddWithValue("@Username", username);

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
                throw new Exception($"Error finding referee by username: {ex.Message}", ex);
            }
        }
    }
}