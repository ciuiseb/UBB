using cs.Utils;
using System.Data.SQLite;

namespace cs;

internal static class Program
{
    /// <summary>
    ///  The main entry point for the application.
    /// </summary>
    [STAThread]
    static void Main()
    {
        
        SQLiteConnection connection = DatabaseConfig.GetConnection();
        ApplicationConfiguration.Initialize();
        Application.Run(new Form1());
    }
}