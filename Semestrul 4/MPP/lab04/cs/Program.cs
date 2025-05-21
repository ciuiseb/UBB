using cs.Utils;
using System.Data.SQLite;
using log4net;
using log4net.Config;
using cs.Repository.Impl;
using cs.Repository.Interfaces;
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

        Console.WriteLine(refereeRepository.FindAll().Count());
        Console.WriteLine(eventRepository.FindAll().Count());
        Console.WriteLine(refereeDisciplineRepository.FindAll().Count());
        Console.WriteLine(refereeDisciplineRepository.FindAll().Count());
        Console.WriteLine(resultRepository.FindAll().Count());

        Application.Run(new Form1());
    }
}