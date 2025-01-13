namespace lab_11.app;

public delegate void LogEventHandler(string message);

public class Logger
{
    private readonly string _file;
    public event LogEventHandler OnLog;

    public Logger(string logPath)
    {
        _file = logPath;
        OnLog += WriteToLog;
    }

    private void WriteToLog(string message)
    {
        var logMessage = $"[{DateTime.Now:yyyy-MM-dd HH:mm:ss}] {message}";
        using var sw = new StreamWriter(_file, true);
        sw.WriteLine(logMessage);
        sw.Flush();
    }

    public void Log(string message)
    {
        OnLog?.Invoke(message);
    }
}
