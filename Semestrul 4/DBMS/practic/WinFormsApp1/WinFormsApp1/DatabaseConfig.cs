using Microsoft.Data.SqlClient;

namespace WinFormsApp1;

public class DatabaseConfig
{
    private const string ConnectionString = "Server=localhost\\SQLEXPRESS;Database=PRACTIC_SGBD;Trusted_Connection=True;TrustServerCertificate=True;";

    public static SqlConnection GetConnection()
    {
        var connection = new SqlConnection(ConnectionString);
        return connection;
    }
}