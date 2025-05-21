using System;
using System.Data;
using System.Configuration;
using System.Data.SQLite;
using System.IO;

namespace Triathlon.Persistence.Utils
{
    public class DatabaseConfig
    {
        private static DatabaseConfig instance;
        private static readonly object lockObject = new object();
        private static SQLiteConnection connection;

        private DatabaseConfig() { }

        public static DatabaseConfig GetInstance()
        {
            if (instance == null)
            {
                lock (lockObject)
                {
                    if (instance == null)
                    {
                        instance = new DatabaseConfig();
                    }
                }
            }
            return instance;
        }

        public static SQLiteConnection GetConnection()
        {
            if (connection == null || connection.State == ConnectionState.Closed)
            {
                try
                {
                    string dbPath = ConfigurationManager.AppSettings["db.url"];
                    if (string.IsNullOrEmpty(dbPath))
                    {
                        throw new IOException("db.url property not found or empty in App.config");
                    }

                    string connectionString = $"Data Source={dbPath};Version=3;";
                    connection = new SQLiteConnection(connectionString);
                    connection.Open();
                }
                catch (Exception ex)
                {
                    throw new Exception("Failed to establish database connection", ex);
                }
            }
            return connection;
        }

        public void CloseConnection()
        {
            if (connection != null && connection.State != ConnectionState.Closed)
            {
                connection.Close();
            }
        }
    }
}