package triathlon.network.jsonprotocol;

import triathlon.model.*;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private RefereeDiscipline refereeDiscipline;
    private List<Result> results;
    private int totalPoints;

    public Response() {
    }

    public ResponseType getType() {
        return type;

    }

    public RefereeDiscipline getRefereeDiscipline() {
        return refereeDiscipline;
    }

    public void setRefereeDiscipline(RefereeDiscipline refereeDiscipline) {
        this.refereeDiscipline = refereeDiscipline;
    }

    public Event getEvent() {
        return refereeDiscipline.getEvent();
    }

    public Discipline getDiscipline() {
        return refereeDiscipline.getDiscipline();
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Referee getReferee() {
        return refereeDiscipline.getReferee();
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
    public int getTotalPoints() {
        return totalPoints;
    }
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", referee=" + getReferee() +
                ", event=" + getEvent() +
                ", discipline=" + getDiscipline() +
                ", results=" + results +
                '}';
    }
}