package triathlon.services;

import java.util.List;
import triathlon.model.*;

public interface ITriathlonServices {
    RefereeDiscipline login(Referee referee, ITriathlonClient client) throws TriathlonException;
    void logout(Referee referee, ITriathlonClient client) throws TriathlonException;
    List<Result> getResultsByEvent(Long eventId);
    void updateResult(Result result);
    int getTotalPointsForParticipant(Long participantId, Long eventId);
}