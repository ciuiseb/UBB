package server;

import triathlon.persistence.interfaces.*;
import triathlon.model.*;
import triathlon.services.ITriathlonObserver;
import triathlon.services.ITriathlonServices;
import triathlon.services.TriathlonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements ITriathlonServices {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final RefereeRepository refereeRepository;
    private final ResultRepository resultRepository;
    private final RefereeDisciplineRepository refereeDisciplineRepository;


    private Map<Long, ITriathlonObserver> loggedReferees;


    private static final Logger logger = LogManager.getLogger(ServiceImpl.class);


    private final int defaultThreadsNo = 5;

    public ServiceImpl(EventRepository eventRepository, ParticipantRepository participantRepository,
                       RefereeRepository refereeRepository, ResultRepository resultRepository,
                       RefereeDisciplineRepository refereeDisciplineRepository) {
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
        this.refereeRepository = refereeRepository;
        this.resultRepository = resultRepository;
        this.refereeDisciplineRepository = refereeDisciplineRepository;
        this.loggedReferees = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized RefereeDiscipline login(Referee tempReferee, ITriathlonObserver client) throws TriathlonException {
        logger.info("Login attempt from referee: " + tempReferee.getUsername());

        Optional<Referee> refereeOpt = refereeRepository.findByUsername(tempReferee.getUsername());
        if (refereeOpt.isEmpty()) {
            logger.error("Referee not found: " + tempReferee.getUsername());
            throw new TriathlonException("Invalid username or password.");
        }

        var refereeDisciplineOpt = refereeDisciplineRepository.findByRefereeId(refereeOpt.get().getId());
        if (refereeDisciplineOpt.isPresent()) {
            Referee existingReferee = refereeOpt.get();

            if (existingReferee.getPassword().equals(tempReferee.getPassword())) {
                if (loggedReferees.containsKey(existingReferee.getId())) {
                    logger.error("Referee already logged in: " + existingReferee.getUsername());
                    throw new TriathlonException("Referee already logged in.");
                }

                loggedReferees.put(existingReferee.getId(), client);
                logger.info("Referee logged in successfully: " + existingReferee.getUsername());

                return refereeDisciplineOpt.get();
            } else {
                logger.error("Invalid password for referee: " + existingReferee.getUsername());
                throw new TriathlonException("Invalid username or password.");
            }
        } else {
            logger.error("Referee doesn't have assigned any discipline: " + refereeOpt.get().getUsername());
            throw new TriathlonException("Invalid username or password.");
        }
    }

    @Override
    public synchronized void logout(Referee referee, ITriathlonObserver client) throws TriathlonException {
        logger.info("Logout attempt from referee: " + referee.getUsername());

        ITriathlonObserver localClient = loggedReferees.remove(referee.getId());

        if (localClient == null) {
            logger.error("Referee not logged in: " + referee.getUsername());
            throw new TriathlonException("Referee is not logged in.");
        }

        logger.info("Referee logged out successfully: " + referee.getUsername());
    }

    @Override
    public List<Result> getResultsByEvent(Long eventId) {
        logger.info("Getting results for event ID: " + eventId);
        return resultRepository.findByEvent(eventId.intValue());
    }

    @Override
    public synchronized void updateResult(Result result) {
        logger.info("Updating result ID: " + result.getId() + " for participant: " + result.getParticipant().getId());

        resultRepository.update(result);
        notifyResultUpdated(result);
    }

    @Override
    public int getTotalPointsForParticipant(Long participantId, Long eventId) {
        return resultRepository.findByParticipant(Math.toIntExact(participantId)).stream()
                .filter(r -> r.getEvent().getId().equals(eventId))
                .mapToInt(Result::getPoints)
                .sum();
    }

    private void notifyResultUpdated(Result result) {
        logger.info("Notifying result update for result ID: " + result.getId());
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (var entry : loggedReferees.entrySet()) {
            var observer = entry.getValue();
            executor.execute(() -> {
                try {
                    logger.debug("Notifying referee ID: " + entry.getKey() + " about result update");
                    observer.resultsUpdated(result);
                } catch (TriathlonException e) {
                    logger.error("Error notifying referee: " + e.getMessage());
                }
            });
        }
        executor.shutdown();
    }
}





