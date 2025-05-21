using System.Collections.Generic;
using Triathlon.Model;
namespace Triathlon.Persistence.Interfaces
{
    public interface IEventRepository : IRepository<long, Event>
    {
        /// <summary>
        /// Gets all events assigned to a specific referee
        /// </summary>
        /// <param name="refereeId">The referee ID</param>
        /// <returns>List of events</returns>
        IEnumerable<Event> FindByRefereeId(long refereeId);
    }
}