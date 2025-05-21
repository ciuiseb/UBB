using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Collections.Concurrent;
using log4net;
using Triathlon.Persistence.Interfaces;
using Triathlon.Model;
using Triathlon.Services;

namespace Triathlon.Server
{
    public class ServiceImplementation : ITriathlonServices
    {
        private readonly IEventRepository _eventRepository;
        private readonly IParticipantRepository _participantRepository;
        private readonly IRefereeRepository _refereeRepository;
        private readonly IResultRepository _resultRepository;
        private readonly IRefereeDisciplineRepository _refereeDisciplineRepository;

                 private ConcurrentDictionary<long, ITriathlonObserver> _loggedReferees;

                 private static readonly ILog _logger = LogManager.GetLogger(typeof(ServiceImplementation));

                 private readonly int _defaultThreadsCount = 5;

        public ServiceImplementation(
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
            _loggedReferees = new ConcurrentDictionary<long, ITriathlonObserver>();
        }

        public RefereeDiscipline Login(Referee tempReferee, ITriathlonObserver client)
        {
            lock (this)
            {
                _logger.Info($"Login attempt from referee: {tempReferee.Username}");

                var refereeOpt = _refereeRepository.FindByUsername(tempReferee.Username);
                if (refereeOpt == null)
                {
                    _logger.Error($"Referee not found: {tempReferee.Username}");
                    throw new TriathlonException("Invalid username or password.");
                }

                var refereeDisciplineOpt = _refereeDisciplineRepository.FindByRefereeId(refereeOpt.Id);
                if (refereeDisciplineOpt != null)
                {
                    Referee existingReferee = refereeOpt;

                    if (existingReferee.Password.Equals(tempReferee.Password))
                    {
                        if (_loggedReferees.ContainsKey(existingReferee.Id))
                        {
                            _logger.Error($"Referee already logged in: {existingReferee.Username}");
                            throw new TriathlonException("Referee already logged in.");
                        }

                        _loggedReferees[existingReferee.Id] = client;
                        _logger.Info($"Referee logged in successfully: {existingReferee.Username}");

                        return refereeDisciplineOpt;
                    }
                    else
                    {
                        _logger.Error($"Invalid password for referee: {existingReferee.Username}");
                        throw new TriathlonException("Invalid username or password.");
                    }
                }
                else
                {
                    _logger.Error($"Referee doesn't have assigned any discipline: {refereeOpt.Username}");
                    throw new TriathlonException("Invalid username or password.");
                }
            }
        }

        public void Logout(Referee referee, ITriathlonObserver client)
        {
            lock (this)
            {
                _logger.Info($"Logout attempt from referee: {referee.Username}");

                if (!_loggedReferees.TryRemove(referee.Id, out ITriathlonObserver localClient))
                {
                    _logger.Error($"Referee not logged in: {referee.Username}");
                    throw new TriathlonException("Referee is not logged in.");
                }

                _logger.Info($"Referee logged out successfully: {referee.Username}");
            }
        }

        public List<Result> GetResultsByEvent(long eventId)
        {
            _logger.Info($"Getting results for event ID: {eventId}");
            return _resultRepository.FindByEventId(eventId).ToList();
        }

        public void UpdateResult(Result result)
        {
            lock (this)
            {
                _logger.Info($"Updating result ID: {result.Id} for participant: {result.Participant.Id}");

                _resultRepository.Update(result);
                NotifyResultUpdated(result);
            }
        }

        public int GetTotalPointsForParticipant(long participantId, long eventId)
        {
            return _resultRepository.FindByParticipantId(participantId)
                .Where(r => r.Event.Id.Equals(eventId))
                .Sum(r => r.Points);
        }

        private void NotifyResultUpdated(Result result)
        {
            _logger.Info($"Notifying result update for result ID: {result.Id}");

                         List<Thread> threads = new List<Thread>();
            _logger.Info($"There are {_loggedReferees.Count} clients connected");
            foreach (var entry in _loggedReferees)
            {
                var observerId = entry.Key;
                var observer = entry.Value;
                _logger.Info($"Notifying observer with ID: {observerId}");

                Thread thread = new Thread(() =>
                {
                    try
                    {
                        _logger.Debug($"Notifying referee ID: {observerId} about result update");
                        observer.ResultsUpdated(result);
                    }
                    catch (TriathlonException ex)
                    {
                        _logger.Error($"Error notifying referee: {ex.Message}");
                    }
                });

                threads.Add(thread);
                thread.Start();
            }

            
        }
    }
}