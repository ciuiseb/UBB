using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using log4net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Triathlon.Model;
using Triathlon.Services;

namespace Triathlon.Network.JsonProtocol;

public class TriathlonClientJsonWorker : ITriathlonObserver, IDisposable
{
    private readonly ITriathlonServices _server;
    private readonly TcpClient _client;
    private readonly NetworkStream _stream;
    private readonly StreamReader _reader;
    private readonly StreamWriter _writer;
    private readonly JsonSerializer _jsonSerializer;
    private volatile bool _connected;

    private static readonly ILog _logger = LogManager.GetLogger(typeof(TriathlonClientJsonWorker));

    private static readonly Response _okResponse = JsonProtocolUtils.CreateOkResponse();

    public TriathlonClientJsonWorker(ITriathlonServices server, TcpClient client)
    {
        _server = server;
        _client = client;
        _jsonSerializer = new JsonSerializer();

        try
        {
            _stream = client.GetStream();
            _reader = new StreamReader(_stream);
            _writer = new StreamWriter(_stream) { AutoFlush = true };
            _connected = true;
            _logger.Info($"Worker initialized for client: {client.Client.RemoteEndPoint}");
        }
        catch (IOException ex)
        {
            _logger.Error("Error initializing worker", ex);
            _connected = false;
            throw;
        }
    }

    public void Run()
    {
        while (_connected)
        {
            try
            {
                                 if (!_client.Connected)
                {
                    _logger.Warn("Socket is closed, ending worker");
                    _connected = false;
                    break;
                }

                                 if (_client.Available > 0 && _reader.Peek() >= 0)
                {
                                         continue;
                }

                                 string requestLine = _reader.ReadLine();
                if (requestLine == null)
                {
                    _logger.Warn("Client disconnected (null read)");
                    _connected = false;
                    break;
                }

                Request request = JsonConvert.DeserializeObject<Request>(requestLine);
                Response response = HandleRequest(request);
                if (response != null)
                {
                    SendResponse(response);
                }
            }
            catch (SocketException se)
            {
                _logger.Error("Socket error, client likely disconnected", se);
                _connected = false;
                break;
            }
            catch (IOException e)
            {
                _logger.Error("IO error during request processing", e);
                _connected = false;
                break;
            }

                         try
            {
                Thread.Sleep(100);              }
            catch (ThreadInterruptedException e)
            {
                _logger.Warn("Worker thread interrupted", e);
                Thread.CurrentThread.Interrupt();              }
        }

                 CloseConnection();
    }

    private void CloseConnection()
    {
        try
        {
            _logger.Info($"Closing connection to client: {_client.Client.RemoteEndPoint}");

            if (_reader != null) _reader.Close();
            if (_writer != null) _writer.Close();
            if (_stream != null) _stream.Close();
            if (_client != null && _client.Connected) _client.Close();
        }
        catch (IOException e)
        {
            _logger.Error("Error closing connection", e);
        }
    }

    public void ResultsUpdated(Result result)
    {
        if (!_connected || !_client.Connected)
        {
            _logger.Warn("Cannot send update to disconnected client");
            throw new TriathlonException("Client is no longer connected");
        }

        Response resp = JsonProtocolUtils.CreateResultUpdatedResponse(result);
        _logger.Debug($"Sending result update to client {_client.Client.RemoteEndPoint} " +
                $"for participant: {result.Participant.Name} " +
                $"with points: {result.Points}");

        try
        {
            SendResponse(resp);
            _logger.Debug($"Successfully sent result update to client: {_client.Client.RemoteEndPoint}");
        }
        catch (IOException e)
        {
            _logger.Error($"Failed to send result update to client: {_client.Client.RemoteEndPoint}", e);
            _connected = false;
            throw new TriathlonException($"Sending error: {e}");
        }
    }

    private Response HandleRequest(Request request)
    {
        Response response = null;

        switch (request.Type)
        {
            case RequestType.LOGIN:
                _logger.Debug($"Login request...{request.Data}");
                var refereeJson = JsonConvert.SerializeObject(request.Data);
                Referee referee = JsonConvert.DeserializeObject<Referee>(refereeJson);
                try
                {
                    RefereeDiscipline assignment = _server.Login(referee, this);
                    return JsonProtocolUtils.CreateLoginResponse(assignment);
                }
                catch (TriathlonException e)
                {
                    _connected = false;
                    return JsonProtocolUtils.CreateErrorResponse(e.Message);
                }

            case RequestType.LOGOUT:
                _logger.Debug($"Logout request {request.Data}");
                var logoutRefereeJson = JsonConvert.SerializeObject(request.Data);
                Referee logoutReferee = JsonConvert.DeserializeObject<Referee>(logoutRefereeJson);
                try
                {
                    _server.Logout(logoutReferee, this);
                    _connected = false;
                    return _okResponse;
                }
                catch (TriathlonException e)
                {
                    return JsonProtocolUtils.CreateErrorResponse(e.Message);
                }

            case RequestType.GET_RESULTS_BY_EVENT:
                _logger.Debug($"GetResultsByEvent request {request.Data}");
                var eventIdJson = JsonConvert.SerializeObject(request.Data);
                long eventId = JsonConvert.DeserializeObject<long>(eventIdJson);
                try
                {
                    List<Result> results = _server.GetResultsByEvent(eventId);
                    return JsonProtocolUtils.CreateGetResultsByEventResponse(results);
                }
                catch (Exception e)
                {
                    return JsonProtocolUtils.CreateErrorResponse(e.Message);
                }

            case RequestType.UPDATE_RESULT:
                _logger.Debug($"UpdateResult request {request.Data}");
                var resultJson = JsonConvert.SerializeObject(request.Data);
                Result result = JsonConvert.DeserializeObject<Result>(resultJson);
                try
                {
                    _server.UpdateResult(result);
                    return _okResponse;
                }
                catch (Exception e)
                {
                    return JsonProtocolUtils.CreateErrorResponse(e.Message);
                }

            case RequestType.GET_TOTAL_POINTS_FOR_PARTICIPANT:
                _logger.Debug($"GetTotalPointsForParticipant request {request.Data}");
                var paramsJson = JsonConvert.SerializeObject(request.Data);
                object[] parameters = JsonConvert.DeserializeObject<object[]>(paramsJson);

                                 long participantId = Convert.ToInt64(parameters[0]);
                long participantEventId = Convert.ToInt64(parameters[1]);

                try
                {
                    int points = _server.GetTotalPointsForParticipant(participantId, participantEventId);
                    response = JsonProtocolUtils.CreateOkResponse();
                    response.TotalPoints = points;
                    return response;
                }
                catch (Exception e)
                {
                    return JsonProtocolUtils.CreateErrorResponse(e.Message);
                }

            default:
                return null;
        }
    }

    private void SendResponse(Response response)
    {
                 if (!_client.Connected)
        {
            _logger.Warn("Attempted to send response on closed socket");
            throw new IOException("Connection is closed");
        }

        string responseLine = JsonConvert.SerializeObject(response);
        _logger.Debug($"Sending response: {responseLine}");

        lock (_writer)
        {
            _writer.WriteLine(responseLine);
            _writer.Flush();
        }
    }

    public void Dispose()
    {
        CloseConnection();
    }
}