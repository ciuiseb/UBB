package mpp.repository.interfaces;

import mpp.domain.RefereeDiscipline;

import java.util.List;

public interface RefereeDisciplineRepository extends Repository<Long, RefereeDiscipline> {
    List<RefereeDiscipline> findByRefereeId(Long refereeId);
    List<RefereeDiscipline> findByEventId(Long eventId);
    List<RefereeDiscipline> findByDisciplineId(Long disciplineId);
    List<RefereeDiscipline> findByRefereeAndEvent(Long refereeId, Long eventId);
    RefereeDiscipline findByRefereeEventAndDiscipline(Long refereeId, Long eventId, Long disciplineId);
}
