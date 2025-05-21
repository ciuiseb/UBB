using Microsoft.Data.SqlClient;

namespace DeadlockScenario;

public class DatabaseConfig
{
    private const string ConnectionString = "Server=localhost\\SQLEXPRESS;Database=SGBD_LAB4;Trusted_Connection=True;TrustServerCertificate=True;";

    public static SqlConnection GetConnection()
    {
        var connection = new SqlConnection(ConnectionString);
        return connection;
    }
}