namespace Triathlon.Model;

public class RefereeDiscipline : TEntity<long>
{
    private Referee referee;
    private Event @event;
    private Discipline discipline;

    public RefereeDiscipline() : base()
    {
    }
    public RefereeDiscipline(long id, Referee referee, Event @event, Discipline discipline) : base(id)
    {
        this.referee = referee;
        this.@event = @event;
        this.discipline = discipline;
    }

    public RefereeDiscipline(Referee referee, Event @event, Discipline discipline)
    {
        this.referee = referee;
        this.@event = @event;
        this.discipline = discipline;
    }

    public Referee Referee
    {
        get { return referee; }
        set { referee = value; }
    }

    public Event Event
    {
        get { return @event; }
        set { @event = value; }
    }

    public Discipline Discipline
    {
        get { return discipline; }
        set { discipline = value; }
    }
}