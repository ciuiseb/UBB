using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Concurrent;
using Newtonsoft.Json;
using log4net;
using Triathlon.Model;
using Triathlon.Services;

namespace Triathlon.Network.JsonProtocol;

public class TriathlonServicesJsonProxy : ITriathlonServices, IDisposable
{
    private readonly string _host;
    private readonly int _port;

    private ITriathlonObserver _client;

    private TcpClient _tcpClient;
    private NetworkStream _stream;
    private StreamWriter _writer;
    private StreamReader _reader;
    private readonly BlockingCollection<Response> _responses;
    private volatile bool _finished;
    private Thread _readerThread;

    private static readonly ILog _logger = LogManager.GetLogger(typeof(TriathlonServicesJsonProxy));

    public TriathlonServicesJsonProxy(string host, int port)
    {
        _host = host;
        _port = port;
        _responses = new BlockingCollection<Response>();
    }

    public void CloseConnection()
    {
        _finished = true;
        try
        {
            if (_reader != null) _reader.Close();
            if (_writer != null) _writer.Close();
            if (_stream != null) _stream.Close();
            if (_tcpClient != null) _tcpClient.Close();

            if (_readerThread != null && _readerThread.IsAlive)
            {
                _readerThread.Interrupt();
            }

            _client = null;
        }
        catch (IOException ex)
        {
            _logger.Warn($"Error closing connection: {ex.Message}");
        }
    }

    private void InitializeConnection()
    {
        try
        {
            _tcpClient = new TcpClient();
            _tcpClient.Connect(_host, _port);

            _stream = _tcpClient.GetStream();
            _writer = new StreamWriter(_stream) { AutoFlush = true };
            _reader = new StreamReader(_stream);
            _finished = false;

            _readerThread = new Thread(ReaderThreadMethod);
            _readerThread.IsBackground = true;
            _readerThread.Start();
        }
        catch (IOException ex)
        {
            _logger.Error($"Error connecting to server: {ex.Message}");
            throw new TriathlonException($"Error connecting to server: {ex.Message}");
        }
    }

    private void SendRequest(Request request)
    {
        if (_tcpClient == null || !_tcpClient.Connected)
        {
            InitializeConnection();
        }

                 string requestJson = JsonConvert.SerializeObject(request);
        _logger.Info($"Sending request: {requestJson}");

        lock (_writer)
        {
            _writer.WriteLine(requestJson);
            _writer.Flush();
        }
    }

    private Response ReadResponse()
    {
        try
        {
                         Response response = _responses.Take();
            _logger.Info($"Received response: {JsonConvert.SerializeObject(response)}");

            if (response.Type == ResponseType.ERROR)
            {
                _logger.Warn($"Server error: {response.ErrorMessage}");
                throw new TriathlonException($"Server error: {response.ErrorMessage}");
            }

            return response;
        }
        catch (InvalidOperationException ex)
        {
            _logger.Error("Collection was closed while waiting for response", ex);
            throw new TriathlonException("Connection closed while waiting for response");
        }
        catch (ThreadInterruptedException ex)
        {
            _logger.Warn($"Interrupted while waiting for response: {ex.Message}");
            Thread.CurrentThread.Interrupt();
            throw new TriathlonException("Interrupted while waiting for response");
        }
    }

    public RefereeDiscipline Login(Referee referee, ITriathlonObserver client)
    {
        InitializeConnection();
        _client = client;
        Request request = JsonProtocolUtils.CreateLoginRequest(referee);
        SendRequest(request);
        Response response = ReadResponse();
        return response.RefereeDiscipline;
    }

    public void Logout(Referee referee, ITriathlonObserver client)
    {
        Request request = JsonProtocolUtils.CreateLogoutRequest(referee);
        SendRequest(request);
        Response response = ReadResponse();
        CloseConnection();
    }

    public List<Result> GetResultsByEvent(long eventId)
    {
        Request request = JsonProtocolUtils.CreateGetResultsByEventRequest(eventId);
        SendRequest(request);
        Response response = ReadResponse();
        return response.Results;
    }

    public void UpdateResult(Result result)
    {
        Request request = JsonProtocolUtils.CreateUpdateResultRequest(result);
        SendRequest(request);
        Response response = ReadResponse();
    }

    public int GetTotalPointsForParticipant(long participantId, long eventId)
    {
        Request request = JsonProtocolUtils.CreateGetTotalPointsForParticipantRequest(participantId, eventId);
        SendRequest(request);
        Response response = ReadResponse();
        return response.TotalPoints;
    }

         private void ReaderThreadMethod()
    {
        _logger.Info("Reader thread started");
        try
        {
            while (!_finished && Thread.CurrentThread.IsAlive)
            {
                                 if (_tcpClient != null && _tcpClient.Connected)
                {
                    try
                    {
                                                 if (_tcpClient.Available > 0 || _reader.Peek() > -1)
                        {
                            string responseJson = _reader.ReadLine();
                            _logger.Debug($"Reader thread read: {responseJson}");

                                                         if (responseJson == null)
                            {
                                _logger.Warn("Received null response, server likely disconnected");
                                throw new IOException("Server disconnected");
                            }

                                                         Response response = JsonConvert.DeserializeObject<Response>(responseJson);

                                                         if (response.Type == ResponseType.RESULT_UPDATED)
                            {
                                HandleResultUpdated(response);
                            }
                            else
                            {
                                                                 _responses.Add(response);
                            }
                        }
                        else
                        {
                                                         Thread.Sleep(10);
                        }
                    }
                    catch (ThreadInterruptedException)
                    {
                        _logger.Warn("Reader thread interrupted");
                        Thread.CurrentThread.Interrupt();
                        break;
                    }
                    catch (IOException ex)
                    {
                        _logger.Error($"IO error in reader thread: {ex.Message}");
                                                 if (!_finished)
                        {
                            _finished = true;
                            break;
                        }
                    }
                }
                else
                {
                                         Thread.Sleep(100);
                }
            }
        }
        catch (Exception ex)
        {
            _logger.Error($"Unexpected error in reader thread: {ex.Message}", ex);
        }
        finally
        {
            _logger.Info("Reader thread exiting");
        }
    }

    private void HandleResultUpdated(Response response)
    {
                 if (_client != null && response.Results != null && response.Results.Count > 0)
        {
            try
            {
                var result = response.Results[0];
                _logger.Info($"Notifying client about result update: " +
                        $"{result.Participant.Name} - {result.Points} points");

                _client.ResultsUpdated(result);
            }
            catch (TriathlonException ex)
            {
                _logger.Warn($"Error notifying client: {ex.Message}");
            }
        }
        else
        {
            _logger.Warn($"Cannot handle result update: client={_client}" +
                    $", result={(response.Results != null && response.Results.Count > 0 ? "present" : "null")}");
        }
    }

    public void Dispose()
    {
        CloseConnection();
    }
}