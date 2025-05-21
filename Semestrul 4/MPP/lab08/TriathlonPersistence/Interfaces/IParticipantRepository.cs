using System.Collections.Generic;
using Triathlon.Model;
namespace Triathlon.Persistence.Interfaces
{
    public interface IParticipantRepository : IRepository<long, Participant>
    {
        /// <summary>
        /// Gets a participant by their full name
        /// </summary>
        /// <param name="name">The participant's full name</param>
        /// <returns>The participant if found, otherwise null</returns>
        Participant FindByName(string name);

        /// <summary>
        /// Gets a participant by their email
        /// </summary>
        /// <param name="email">The participant's email</param>
        /// <returns>The participant if found, otherwise null</returns>
        Participant FindByEmail(string email);
    }
}