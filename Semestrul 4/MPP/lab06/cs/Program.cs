using cs.Utils;
using System.Data.SQLite;
using log4net;
using log4net.Config;
using cs.Repository.Impl;
using cs.Repository.Interfaces;
using cs.Service;
using cs.GUI;
namespace cs;

internal static class Program
{
    private static readonly ILog log = LogManager.GetLogger(typeof(Program));
    /// <summary>
    ///  The main entry point for the application.
    /// </summary>
    [STAThread]
    static void Main()
    {
        log.Info("Starting..");
        
        SQLiteConnection connection = DatabaseConfig.GetConnection();
        ApplicationConfiguration.Initialize();

        IRefereeRepository refereeRepository = new RefereeRepositoryImpl();
        IEventRepository eventRepository = new EventRepositoryImpl();
        IRefereeDisciplineRepository refereeDisciplineRepository = new RefereeDisciplineRepositoryImpl(refereeRepository, eventRepository);
        IParticipantRepository participantRepository = new ParticipantRepositoryImpl();
        IResultRepository resultRepository = new ResultRepositoryImpl(eventRepository, participantRepository);

        IService service = new ServiceImpl(
                eventRepository,
                participantRepository,
                refereeRepository,
                resultRepository,
                refereeDisciplineRepository
            );

        var login = new LoginForm();
        login.SetService( service );
        Application.Run(login);
    }
}