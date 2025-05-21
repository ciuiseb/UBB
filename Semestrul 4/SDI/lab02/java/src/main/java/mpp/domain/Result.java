package mpp.domain;

public class Result extends Entity<Long>{
    //event, participant, swimming_time, cycling_time, running_time
    private Event event;
    private Participant participant;
    private String swimming_time;
    private String cycling_time;
    private String running_time;

    public Result(Long aLong, Event event, Participant participant, String swimming_time, String cycling_time, String running_time) {
        super(aLong);
        this.event = event;
        this.participant = participant;
        this.swimming_time = swimming_time;
        this.cycling_time = cycling_time;
        this.running_time = running_time;
    }

    public Result(Event event, Participant participant, String swimming_time, String cycling_time, String running_time) {
        this.event = event;
        this.participant = participant;
        this.swimming_time = swimming_time;
        this.cycling_time = cycling_time;
        this.running_time = running_time;
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

    public String getSwimming_time() {
        return swimming_time;
    }

    public void setSwimming_time(String swimming_time) {
        this.swimming_time = swimming_time;
    }

    public String getCycling_time() {
        return cycling_time;
    }

    public void setCycling_time(String cycling_time) {
        this.cycling_time = cycling_time;
    }

    public String getRunning_time() {
        return running_time;
    }

    public void setRunning_time(String running_time) {
        this.running_time = running_time;
    }
}
