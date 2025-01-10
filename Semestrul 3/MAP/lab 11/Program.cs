using lab_11.app;
using lab_11.tests;

namespace lab_11;

class Program
{
    public static void Main(string[] args)
    {
        TestRunner.RunTests();
        const string connectionString = "Host=localhost;Database=lab_nba_league;Username=postgres;Password=7979";

        var app = new App(new Controller(connectionString));
        app.Start();

    }
}