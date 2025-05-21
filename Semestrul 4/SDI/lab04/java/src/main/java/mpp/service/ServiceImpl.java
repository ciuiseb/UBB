package mpp.service;

import mpp.domain.*;
import mpp.repository.interfaces.*;

import java.util.List;
import java.util.Optional;

public class ServiceImpl implements Service {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final RefereeRepository refereeRepository;
    private final ResultRepository resultRepository;
    private final RefereeDisciplineRepository refereeDisciplineRepository;

    public ServiceImpl(EventRepository eventRepository, ParticipantRepository participantRepository,
                       RefereeRepository refereeRepository, ResultRepository resultRepository, RefereeDisciplineRepository refereeDisciplineRepository) {
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
        this.refereeRepository = refereeRepository;
        this.resultRepository = resultRepository;
        this.refereeDisciplineRepository = refereeDisciplineRepository;
    }

    @Override
    public Referee login(String username, String password) {
        Optional<Referee> refereeOpt = refereeRepository.findByUsername(username);
        if (refereeOpt.isPresent()) {
            Referee referee = refereeOpt.get();
            if (referee.getPassword().equals(password)) {
                return referee;
            }
        }
        return null;
    }

    @Override
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findOne(id).orElse(null);
    }

    @Override
    public List<Event> getEventsByReferee(Long refereeId) {
        return eventRepository.findByRefereeId(refereeId.intValue());
    }

    @Override
    public List<Participant> getAllParticipants() {
        return (List<Participant>) participantRepository.findAll();
    }

    @Override
    public Participant getParticipantById(Long id) {
        return participantRepository.findOne(id).orElse(null);
    }

    @Override
    public Participant getParticipantByName(String name) {
        return participantRepository.findByName(name);
    }

    @Override
    public List<Result> getResultsByEvent(Long eventId) {
        return resultRepository.findByEvent(eventId.intValue());
    }

    @Override
    public List<Result> getResultsByParticipant(Long participantId) {
        return resultRepository.findByParticipant(participantId.intValue());
    }

    @Override
    public void saveResult(Result result) {
        resultRepository.save(result);
    }

    @Override
    public void updateResult(Result result) {
        resultRepository.update(result);
    }

    @Override
    public void deleteResult(Long id) {
        resultRepository.delete(id);
    }

    @Override
    public List<Referee> getAllReferees() {
        return (List<Referee>) refereeRepository.findAll();
    }

    @Override
    public Referee getRefereeById(Long id) {
        return refereeRepository.findOne(id).orElse(null);
    }

    @Override
    public Referee getRefereeByUsername(String username) {
        return refereeRepository.findByUsername(username).orElse(null);
    }

    @Override
    public RefereeDiscipline findByRefereeEventAndDiscipline(Long refereeId, Long eventId, Long disciplineId) {
        return refereeDisciplineRepository.findByRefereeEventAndDiscipline(refereeId, eventId, disciplineId);
    }
    @Override
    public List<RefereeDiscipline> getRefereeDisciplines(Long refereeId) {
        return refereeDisciplineRepository.findByRefereeId(refereeId);
    }
}