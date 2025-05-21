using System.Collections.Generic;
using Triathlon.Model;
namespace Triathlon.Persistence.Interfaces
{
    public interface IResultRepository : IRepository<long, Result>
    {
        /// <summary>
        /// Gets all results for a specific event
        /// </summary>
        /// <param name="eventId">The event ID</param>
        /// <returns>List of results</returns>
        IEnumerable<Result> FindByEventId(long eventId);

        /// <summary>
        /// Gets all results for a specific participant
        /// </summary>
        /// <param name="participantId">The participant ID</param>
        /// <returns>List of results</returns>
        IEnumerable<Result> FindByParticipantId(long participantId);

        /// <summary>
        /// Gets all results for a specific participant in a specific event
        /// </summary>
        /// <param name="participantId">The participant ID</param>
        /// <param name="eventId">The event ID</param>
        /// <returns>List of results</returns>
        IEnumerable<Result> FindByParticipantAndEvent(long participantId, long eventId);
    }
}