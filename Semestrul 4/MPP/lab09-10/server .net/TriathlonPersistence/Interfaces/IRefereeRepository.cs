using Triathlon.Model;
namespace Triathlon.Persistence.Interfaces
{
    public interface IRefereeRepository : IRepository<long, Referee>
    {
        /// <summary>
        /// Gets a referee by username
        /// </summary>
        /// <param name="username">The username</param>
        /// <returns>The referee if found, otherwise null</returns>
        Referee FindByUsername(string username);
    }
}