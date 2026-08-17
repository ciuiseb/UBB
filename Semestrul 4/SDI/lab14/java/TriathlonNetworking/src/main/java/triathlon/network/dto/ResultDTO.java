package triathlon.network.dto;

import triathlon.model.Discipline;
import java.io.Serializable;

public class ResultDTO implements Serializable {
    private Long id;
    private Long eventId;
    private Long participantId;
    private Discipline discipline;
    private int points;

    public ResultDTO() {}

    public ResultDTO(Long id, Long eventId, Long participantId, Discipline discipline, int points) {
        this.id = id;
        this.eventId = eventId;
        this.participantId = participantId;
        this.discipline = discipline;
        this.points = points;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }
    public Discipline getDiscipline() { return discipline; }
    public void setDiscipline(Discipline discipline) { this.discipline = discipline; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}