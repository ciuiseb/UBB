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
    public List<Result> getResultsByEvent(Long eventId) {
        return resultRepository.findByEvent(eventId.intValue());
    }

    @Override
    public void updateResult(Result result) {
        resultRepository.update(result);
    }
    @Override
    public List<RefereeDiscipline> getRefereeDisciplines(Long refereeId) {
        return refereeDisciplineRepository.findByRefereeId(refereeId);
    }
}