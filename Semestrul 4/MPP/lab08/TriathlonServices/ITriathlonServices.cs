using Triathlon.Model;

namespace Triathlon.Services
{
    public interface ITriathlonServices
    {
        RefereeDiscipline Login(Referee referee, ITriathlonObserver client);
        void Logout(Referee referee, ITriathlonObserver client);
        List<Result> GetResultsByEvent(long eventId);
        void UpdateResult(Result result);
        int GetTotalPointsForParticipant(long participantId, long eventId);
    }
}