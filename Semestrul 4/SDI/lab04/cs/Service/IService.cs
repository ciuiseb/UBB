using System.Collections.Generic;
using cs.Domain;

namespace cs.Service;

public interface IService
{
    Referee Login(string username, string password);
    List<Event> GetAllEvents();
    List<Result> GetResultsByEvent(long eventId);
    void UpdateResult(Result result);
    List<RefereeDiscipline> GetRefereeDisciplines(long refereeId);
}