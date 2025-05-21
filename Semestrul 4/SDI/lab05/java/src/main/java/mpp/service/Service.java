package mpp.service;

import mpp.domain.Event;
import mpp.domain.Referee;
import mpp.domain.RefereeDiscipline;
import mpp.domain.Result;

import java.util.List;

public interface Service {
    Referee login(String username, String password);

    List<Event> getAllEvents();

    List<Result> getResultsByEvent(Long eventId);
    void updateResult(Result result);

    List<RefereeDiscipline> getRefereeDisciplines(Long refereeId);

}
