using System;
using System.Data;
using System.Data.SQLite;
using cs.Domain;
using cs.Repository.Abstracts;
using cs.Repository.Interfaces;

namespace cs.Repository.Impl
{
    public class RefereeRepositoryImpl : AbstractDBRepository<long, Referee>, IRefereeRepository
    {
        public RefereeRepositoryImpl() : base("Referees", "id")
        {
        }

        protected override string GetInsertParameters()
        {
            return "username, password, email, first_name, last_name";
        }

        protected override string GetUpdateParameters()
        {
            return "username=@username, password=@password, email=@email, first_name=@firstName, last_name=@lastName";
        }

        protected override int GetUpdateParameterCount()
        {
            return 5;
        }

        protected override Referee CreateEntity(IDataReader reader)
        {
            long id = Convert.ToInt64(reader["id"]);
            string username = reader["username"].ToString();
            string password = reader["password"].ToString();
            string email = reader["email"].ToString();
            string firstName = reader["first_name"].ToString();
            string lastName = reader["last_name"].ToString();

            Referee referee = new Referee(username, password, email, firstName, lastName);
            referee.SetId(id);
            return referee;
        }

        protected override void SetParameters(SQLiteCommand command, Referee entity)
        {
            command.Parameters.AddWithValue("@username", entity.GetUsername());
            command.Parameters.AddWithValue("@password", entity.GetPassword());
            command.Parameters.AddWithValue("@email", entity.GetEmail());
            command.Parameters.AddWithValue("@firstName", entity.GetFirstName());
            command.Parameters.AddWithValue("@lastName", entity.GetLastName());
        }

        public Referee? FindByUsername(string username)
        {
            string sql = $"SELECT * FROM {_tableName} WHERE username = @username";

            try
            {
                using var command = new SQLiteCommand(sql, _connection);
                command.Parameters.AddWithValue("@username", username);

                using var reader = command.ExecuteReader();
                if (reader.Read())
                {
                    return CreateEntity(reader);
                }
            }
            catch (Exception ex)
            {
                // Logger.LogError($"Error finding referee by username: {ex.Message}");
            }

            return null;
        }
    }
}