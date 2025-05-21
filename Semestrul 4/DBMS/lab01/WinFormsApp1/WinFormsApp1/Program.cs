using System;
using System.Data;
using FactoryProducts;
using Npgsql;

namespace PostgreSQLConnection;

public class DatabaseConnector
{
    private readonly string _connectionString;

    public DatabaseConnector()
    {
        _connectionString = "Host=localhost;Port=5432;Database=sgbd;Username=postgres;Password=7979";
    }

    public void TestConnection()
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();
                Console.WriteLine("Successfully connected to PostgreSQL database!");

                // Example query
                using (NpgsqlCommand command = new NpgsqlCommand("SELECT * FROM Factories", connection))
                {
                    using (NpgsqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Console.WriteLine($"Factory ID: {reader["id"]}, Name: {reader["name"]}");
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error: {ex.Message}");
        }
    }

    public DataTable GetFactories()
    {
        DataTable dataTable = new DataTable();

        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();
                string query = "SELECT * FROM Factories";

                using (NpgsqlDataAdapter adapter = new NpgsqlDataAdapter(query, connection))
                {
                    adapter.Fill(dataTable);
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error retrieving factories: {ex.Message}");
        }

        return dataTable;
    }
}

static class Program
{
    [STAThread]
    static void Main()
    {
        Application.EnableVisualStyles();
        Application.SetCompatibleTextRenderingDefault(false);
        Application.Run(new MainForm());
    }
}