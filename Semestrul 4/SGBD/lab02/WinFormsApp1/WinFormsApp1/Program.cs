using System;
using System.Data;
using ParentsChildren;
using Npgsql;
using System.Configuration;

namespace PostgreSQLConnection;

public class DatabaseConnector
{
    private readonly string _connectionString;

    public DatabaseConnector()
    {
         _connectionString = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
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