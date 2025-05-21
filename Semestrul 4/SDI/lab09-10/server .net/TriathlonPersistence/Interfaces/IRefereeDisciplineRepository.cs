using System.Collections.Generic;
using Triathlon.Model;
namespace Triathlon.Persistence.Interfaces
{
    public interface IRefereeDisciplineRepository : IRepository<long, RefereeDiscipline>
    {
        /// <summary>
        /// Gets a referee discipline assignment by referee ID
        /// </summary>
        /// <param name="refereeId">The referee ID</param>
        /// <returns>The referee discipline assignment if found, otherwise null</returns>
        RefereeDiscipline FindByRefereeId(long refereeId);

        /// <summary>
        /// Gets all referee discipline assignments for an event
        /// </summary>
        /// <param name="eventId">The event ID</param>
        /// <returns>List of referee discipline assignments</returns>
        IEnumerable<RefereeDiscipline> FindByEventId(long eventId);

        /// <summary>
        /// Gets all referee discipline assignments for a discipline
        /// </summary>
        /// <param name="disciplineId">The discipline ID</param>
        /// <returns>List of referee discipline assignments</returns>
        IEnumerable<RefereeDiscipline> FindByDisciplineId(long disciplineId);

        /// <summary>
        /// Gets all referee discipline assignments for a referee and event
        /// </summary>
        /// <param name="refereeId">The referee ID</param>
        /// <param name="eventId">The event ID</param>
        /// <returns>List of referee discipline assignments</returns>
        IEnumerable<RefereeDiscipline> FindByRefereeAndEvent(long refereeId, long eventId);

        /// <summary>
        /// Gets a specific referee discipline assignment
        /// </summary>
        /// <param name="refereeId">The referee ID</param>
        /// <param name="eventId">The event ID</param>
        /// <param name="disciplineId">The discipline ID</param>
        /// <returns>The referee discipline assignment if found, otherwise null</returns>
        RefereeDiscipline FindByRefereeEventAndDiscipline(long refereeId, long eventId, long disciplineId);
    }
}