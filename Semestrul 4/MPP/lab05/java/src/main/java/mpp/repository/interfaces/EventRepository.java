package mpp.repository.interfaces;

import mpp.domain.Event;

import java.util.List;

public interface EventRepository extends Repository<Long, Event>{
    List<Event> findByRefereeId(Integer refereeId);

}
