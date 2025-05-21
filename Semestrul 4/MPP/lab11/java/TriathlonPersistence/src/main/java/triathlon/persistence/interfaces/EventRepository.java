package triathlon.persistence.interfaces;

import triathlon.model.Event;

import java.util.List;

public interface EventRepository extends Repository<Long, Event>{
    List<Event> findByRefereeId(Long refereeId);
}
