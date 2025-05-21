namespace cs.Repository.Interfaces;

using cs.Domain;

public interface IRefereeRepository : IRepository<long, Referee>
{
    Referee FindByUsername(string username);
}