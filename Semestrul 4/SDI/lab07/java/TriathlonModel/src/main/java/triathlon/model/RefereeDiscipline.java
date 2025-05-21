package triathlon.model;


public class RefereeDiscipline extends Entity<Long> {
    private Referee referee;
    private Event event;
    private Discipline discipline;

    public RefereeDiscipline(Long aLong, Referee referee, Event event, Discipline discipline) {
        super(aLong);
        this.referee = referee;
        this.event = event;
        this.discipline = discipline;
    }

    public RefereeDiscipline(Referee referee, Event event, Discipline discipline) {
        this.referee = referee;
        this.event = event;
        this.discipline = discipline;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
