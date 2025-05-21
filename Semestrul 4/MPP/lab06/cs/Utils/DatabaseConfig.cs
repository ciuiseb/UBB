using System;
using System.IO;
using System.Data.SQLite;
using System.Linq;
using System.Collections.Generic;

namespace cs.Utils;

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
        if (true)
        {
            try
            {
                string configPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "configs", "config.properties");

                if (!File.Exists(configPath))
                {
                    throw new IOException("Unable to find config.properties in the configs directory");
                }

                var properties = File.ReadAllLines(configPath)
                    .Where(line => !string.IsNullOrWhiteSpace(line) && !line.TrimStart().StartsWith("#"))
                    .Select(line => line.Split('=', 2))
                    .Where(parts => parts.Length == 2)
                    .ToDictionary(parts => parts[0].Trim(), parts => parts[1].Trim());

                if (!properties.TryGetValue("db.url", out string jdbcUrl) || string.IsNullOrEmpty(jdbcUrl))
                {
                    throw new IOException("db.url property not found or empty in config.properties");
                }

                string connectionString = $"Data Source={jdbcUrl};Version=3;";

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
        if (connection != null && connection.State == System.Data.ConnectionState.Open)
        {
            connection.Close();
        }
    }
}