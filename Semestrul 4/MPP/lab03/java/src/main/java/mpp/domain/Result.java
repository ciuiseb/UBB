package mpp.domain;

public class Result extends Entity<Long>{

    private Event event;
    private Participant participant;
    private Discipline discipline;
    private int points;

    public Result(Long aLong, Event event, Participant participant, Discipline discipline, int points) {
        super(aLong);
        this.event = event;
        this.participant = participant;
        this.discipline = discipline;
        this.points = points;
    }

    public Result(Event event, Participant participant, Discipline discipline, int points) {
        this.event = event;
        this.participant = participant;
        this.discipline = discipline;
        this.points = points;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Discipline getDiscipline() {
        return discipline;
    }
    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}
