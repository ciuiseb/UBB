namespace Triathlon.Model;

public class Result : TEntity<long>
{
    private Event @event;
    private Participant participant;
    private Discipline discipline;
    private int points;

    public Result(): base() { }
    public Result(long id, Event @event, Participant participant, Discipline discipline, int points) : base(id)
    {
        this.@event = @event;
        this.participant = participant;
        this.discipline = discipline;
        this.points = points;
    }

    public Result(Event @event, Participant participant, Discipline discipline, int points)
    {
        this.@event = @event;
        this.participant = participant;
        this.discipline = discipline;
        this.points = points;
    }

    public Event Event
    {
        get { return @event; }
        set { @event = value; }
    }

    public Participant Participant
    {
        get { return participant; }
        set { participant = value; }
    }

    public Discipline Discipline
    {
        get { return discipline; }
        set { discipline = value; }
    }

    public int Points
    {
        get { return points; }
        set { points = value; }
    }
    public override string ToString()
    {
        return $"Score: {Points}"; 
    }
}