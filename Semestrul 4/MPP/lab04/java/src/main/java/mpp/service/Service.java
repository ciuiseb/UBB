package mpp.service;

import mpp.domain.*;

import java.util.List;

public interface Service {
    Referee login(String username, String password);

    List<Event> getAllEvents();
    Event getEventById(Long id);
    List<Event> getEventsByReferee(Long refereeId);


    List<Participant> getAllParticipants();
    Participant getParticipantById(Long id);
    Participant getParticipantByName(String name);


    List<Result> getResultsByEvent(Long eventId);
    List<Result> getResultsByParticipant(Long participantId);
    void saveResult(Result result);
    void updateResult(Result result);
    void deleteResult(Long id);

    List<Referee> getAllReferees();
    Referee getRefereeById(Long id);
    Referee getRefereeByUsername(String username);


    RefereeDiscipline findByRefereeEventAndDiscipline(Long refereeId, Long eventId, Long disciplineId);
    List<RefereeDiscipline> getRefereeDisciplines(Long refereeId);

}
