namespace cs.Domain;

public class Result : IEntity<long>
{
    private Event @event;
    private Participant participant;
    private Discipline discipline;
    private int points;

    public Result(long id, Event @event, Participant participant, Discipline discipline, int points)
        : base(id)
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

    public Event GetEvent()
    {
        return @event;
    }

    public void SetEvent(Event @event)
    {
        this.@event = @event;
    }

    public Participant GetParticipant()
    {
        return participant;
    }

    public void SetParticipant(Participant participant)
    {
        this.participant = participant;
    }

    public Discipline GetDiscipline()
    {
        return discipline;
    }

    public void SetDiscipline(Discipline discipline)
    {
        this.discipline = discipline;
    }

    public int GetPoints()
    {
        return points;
    }

    public void SetPoints(int points)
    {
        this.points = points;
    }
}