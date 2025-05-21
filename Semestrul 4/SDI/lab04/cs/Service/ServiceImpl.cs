using cs.Domain;
using cs.Repository.Interfaces;

namespace cs.Service;

public class ServiceImpl : IService
{
    private readonly IEventRepository _eventRepository;
    private readonly IParticipantRepository _participantRepository;
    private readonly IRefereeRepository _refereeRepository;
    private readonly IResultRepository _resultRepository;
    private readonly IRefereeDisciplineRepository _refereeDisciplineRepository;

    public ServiceImpl(
        IEventRepository eventRepository,
        IParticipantRepository participantRepository,
        IRefereeRepository refereeRepository,
        IResultRepository resultRepository,
        IRefereeDisciplineRepository refereeDisciplineRepository)
    {
        _eventRepository = eventRepository;
        _participantRepository = participantRepository;
        _refereeRepository = refereeRepository;
        _resultRepository = resultRepository;
        _refereeDisciplineRepository = refereeDisciplineRepository;
    }

    public Referee Login(string username, string password)
    {
        Referee referee = _refereeRepository.FindByUsername(username);
        if (referee != null)
        {
            if (referee.GetPassword().Equals(password))
            {
                return referee;
            }
        }
        return null;
    }

    public List<Event> GetAllEvents()
    {
        return _eventRepository.FindAll().ToList();
    }

    public List<Result> GetResultsByEvent(long eventId)
    {
        return _resultRepository.FindByEvent((int)eventId);
    }

    public void UpdateResult(Result result)
    {
        _resultRepository.Update(result);
    }

    public List<RefereeDiscipline> GetRefereeDisciplines(long refereeId)
    {
        return _refereeDisciplineRepository.FindByRefereeId(refereeId);
    }
}