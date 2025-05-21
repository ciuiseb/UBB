namespace cs.Repository.Interfaces;

using cs.Domain;

public interface IParticipantRepository : IRepository<long, Participant>
{
    Participant FindByName(string name);

    Participant FindByEmail(string email);
}