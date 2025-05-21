package triathlon.persistence.interfaces;

import triathlon.model.Result;

import java.util.List;

public interface ResultRepository extends Repository<Long, Result>{

    List<Result> findByEvent(Integer eventId);
    List<Result> findByParticipant(Integer participantId);
}
