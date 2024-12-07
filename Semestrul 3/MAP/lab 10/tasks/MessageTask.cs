namespace lab10;

public class MessageTask(
    string taskId,
    string descriere,
    string mesaj,
    string from,
    string to,
    DateTime date) : Task(taskId, descriere)
{
    public string Mesaj
    {
        get { return mesaj; }
        set { mesaj = value; }
    }

    public string From
    {
        get { return from; }
        set { from = value; }
    }

    public string To
    {
        get { return to; }
        set { to = value; }
    }

    public DateTime Date
    {
        get { return date; }
        set { date = value; }
    }

    public override void Execute()
    {
        string formattedDate = date.ToString("yyyy-MM-dd HH:mm:ss"); // Formatare
        Console.WriteLine($"Id: {TaskID}\n" +
                          $"Mesaj: {mesaj}\n" +
                          $"De la: {from}\n" +
                          $"Către: {to}\n" +
                          $"Data: {formattedDate}");
    }

    public override string ToString()
    {
        return $"{base.ToString()}, Mesaj: {mesaj}, From: {from}, To: {to}, Date: {date}";
    }
}