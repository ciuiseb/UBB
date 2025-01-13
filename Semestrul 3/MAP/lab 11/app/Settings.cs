using System.Text.Json;

namespace lab_11;

public class Settings
{
    public string? LogFilePath { get; }
    public string? ConnectionString { get; }

    public Settings()
    {
        using var document = JsonDocument.Parse(File.ReadAllText("appsettings.json"));
        LogFilePath = document.RootElement
            .GetProperty("LogSettings")
            .GetProperty("LogFilePath")
            .GetString();
        ConnectionString = document.RootElement
            .GetProperty("ConnectionStrings")
            .GetProperty("DefaultConnection")
            .GetString();
    }
}