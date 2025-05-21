namespace cs.Repository.Interfaces;

using cs.Domain;
using System.Collections.Generic;

public interface IRefereeDisciplineRepository : IRepository<long, RefereeDiscipline>
{
    List<RefereeDiscipline> FindByRefereeId(long refereeId);
    List<RefereeDiscipline> FindByEventId(long eventId);
    List<RefereeDiscipline> FindByDisciplineId(long disciplineId);
    List<RefereeDiscipline> FindByRefereeAndEvent(long refereeId, long eventId);
    RefereeDiscipline FindByRefereeEventAndDiscipline(long refereeId, long eventId, long disciplineId);
}