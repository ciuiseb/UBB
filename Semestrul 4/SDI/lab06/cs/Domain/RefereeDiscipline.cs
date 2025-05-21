namespace cs.Domain;

public class RefereeDiscipline : IEntity<long>
{
    //referee, event, discipline
    private Referee referee;
    private Event @event;
    private Discipline discipline;

    public RefereeDiscipline(long id, Referee referee, Event @event, Discipline discipline)
        : base(id)
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

    public Referee GetReferee()
    {
        return referee;
    }

    public void SetReferee(Referee referee)
    {
        this.referee = referee;
    }

    public Event GetEvent()
    {
        return @event;
    }

    public void SetEvent(Event @event)
    {
        this.@event = @event;
    }

    public Discipline GetDiscipline()
    {
        return discipline;
    }

    public void SetDiscipline(Discipline discipline)
    {
        this.discipline = discipline;
    }
}