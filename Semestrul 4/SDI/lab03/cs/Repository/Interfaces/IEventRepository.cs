namespace cs.Repository.Interfaces;

using cs.Domain;
using System.Collections.Generic;

public interface IEventRepository : IRepository<long, Event>
{
    List<Event> FindByRefereeId(int refereeId);
}