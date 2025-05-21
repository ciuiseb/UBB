namespace cs.Domain;

public class Event : Entity<long>
{
    private string name;
    private string date;
    private string location;
    private string description;

    public Event(long id, string name, string date, string location, string description)
        : base(id)
    {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public string GetName()
    {
        return name;
    }

    public void SetName(string name)
    {
        this.name = name;
    }

    public string GetDate()
    {
        return date;
    }

    public void SetDate(string date)
    {
        this.date = date;
    }

    public string GetLocation()
    {
        return location;
    }

    public void SetLocation(string location)
    {
        this.location = location;
    }

    public string GetDescription()
    {
        return description;
    }

    public void SetDescription(string description)
    {
        this.description = description;
    }
}