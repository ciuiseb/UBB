using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Configuration;
using log4net;
using Triathlon.Server;
using Triathlon.Network.JsonProtocol;
using Triathlon.Persistence.Impl;
using Triathlon.Services;

namespace Triathlon.Server;

public class StartJsonServer
{
    private static readonly ILog _logger = LogManager.GetLogger(typeof(StartJsonServer));
    private const int DEFAULT_PORT = 55555;
    private const int THREAD_POOL_SIZE = 10;

    public static void Main(string[] args)
    {
        log4net.Config.XmlConfigurator.Configure(new FileInfo("log4net.config"));
        var eventRepository = new EventRepositoryImpl();
        var participantRepository = new ParticipantRepositoryImpl();
        var resultRepository = new ResultRepositoryImpl(eventRepository, participantRepository);
        var refereeRepository = new RefereeRepositoryImpl();
        var refereeDisciplineRepository = new RefereeDisciplineRepositoryImpl(refereeRepository, eventRepository);

        ITriathlonServices serverImpl = new ServiceImplementation(eventRepository, participantRepository, refereeRepository, resultRepository, refereeDisciplineRepository);

                 int port = DEFAULT_PORT;
        try
        {
            string portString = ConfigurationManager.AppSettings["triathlon.server.port"];
            if (!string.IsNullOrEmpty(portString))
            {
                port = int.Parse(portString);
                _logger.Info("Using port from configuration: " + port);
            }
        }
        catch (Exception ex)
        {
            _logger.Error("Error reading configuration", ex);
        }

                 try
        {
            if (args.Length > 0)
            {
                port = int.Parse(args[0]);
            }
        }
        catch (FormatException ex)
        {
            _logger.Error("Invalid port number", ex);
            Console.WriteLine("Invalid port number. Using default port " + DEFAULT_PORT);
        }

        _logger.Info("Starting server on port: " + port);
        Console.WriteLine("Starting server on port: " + port);

                 ThreadPool.SetMaxThreads(THREAD_POOL_SIZE, THREAD_POOL_SIZE);

                 using (var cancellationTokenSource = new CancellationTokenSource())
        {
            try
            {
                                 TcpListener listener = new TcpListener(IPAddress.Any, port);
                listener.Start();

                _logger.Info("Server started. Waiting for clients...");
                Console.WriteLine("Server started. Waiting for clients...");

                                 Console.CancelKeyPress += (sender, e) => {
                    e.Cancel = true;
                    cancellationTokenSource.Cancel();
                };

                                 while (!cancellationTokenSource.Token.IsCancellationRequested)
                {
                    try
                    {
                        TcpClient client = listener.AcceptTcpClient();
                        _logger.Info($"Client connected from: {((IPEndPoint)client.Client.RemoteEndPoint).Address}");
                        Console.WriteLine($"Client connected from: {((IPEndPoint)client.Client.RemoteEndPoint).Address}");

                                                 TriathlonClientJsonWorker worker = new TriathlonClientJsonWorker(serverImpl, client);
                        ThreadPool.QueueUserWorkItem(_ => {
                            try
                            {
                                worker.Run();
                            }
                            catch (Exception ex)
                            {
                                _logger.Error("Error in worker thread", ex);
                            }
                        });
                    }
                    catch (SocketException ex)
                    {
                        if (!cancellationTokenSource.Token.IsCancellationRequested)
                        {
                            _logger.Error("Error accepting client", ex);
                            Console.WriteLine("Error accepting client: " + ex.Message);
                        }
                    }
                    catch (Exception ex)
                    {
                        _logger.Error("Unexpected error accepting client", ex);
                        Console.WriteLine("Unexpected error accepting client: " + ex.Message);
                    }
                }

                                 listener.Stop();
            }
            catch (Exception ex)
            {
                _logger.Error("Error starting server", ex);
                Console.WriteLine("Error starting server: " + ex.Message);
            }
            finally
            {
                _logger.Info("Server stopped");
                Console.WriteLine("Server stopped");
            }
        }
    }
}