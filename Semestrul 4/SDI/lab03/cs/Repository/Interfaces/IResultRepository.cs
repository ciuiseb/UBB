namespace cs.Repository.Interfaces;

using cs.Domain;
using System.Collections.Generic;

public interface IResultRepository : IRepository<long, Result>
{
    List<Result> FindByEvent(int eventId);
    List<Result> FindByParticipant(int participantId);
}