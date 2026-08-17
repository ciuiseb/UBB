package triathlon.persistence.interfaces;

import triathlon.model.RefereeDiscipline;

import java.util.List;
import java.util.Optional;

public interface RefereeDisciplineRepository extends Repository<Long, RefereeDiscipline> {
    Optional<RefereeDiscipline> findByRefereeId(Long refereeId);
    List<RefereeDiscipline> findByEventId(Long eventId);
    List<RefereeDiscipline> findByDisciplineId(Long disciplineId);
    List<RefereeDiscipline> findByRefereeAndEvent(Long refereeId, Long eventId);
    RefereeDiscipline findByRefereeEventAndDiscipline(Long refereeId, Long eventId, Long disciplineId);
}
