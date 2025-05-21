package triathlon.network.jsonprotocol;

import triathlon.model.Referee;
import triathlon.model.RefereeDiscipline;
import triathlon.model.Result;
import triathlon.services.ITriathlonObserver;
import triathlon.services.ITriathlonServices;
import triathlon.services.TriathlonException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class TriathlonClientJsonWorker implements Runnable, ITriathlonObserver {
    private ITriathlonServices server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    private static final Logger logger = LogManager.getLogger(TriathlonClientJsonWorker.class);

    public TriathlonClientJsonWorker(ITriathlonServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new Gson();
        try {

            output = new PrintWriter(connection.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
            logger.info("Worker initialized for client: " + connection.getInetAddress());
        } catch (IOException e) {
            logger.error("Error initializing worker", e);
            connected = false;
        }
    }

    public void run() {
        while (connected) {
            try {

                if (connection.isClosed()) {
                    logger.warn("Socket is closed, ending worker");
                    connected = false;
                    break;
                }


                if (!input.ready()) {

                    continue;
                }


                String requestLine = input.readLine();
                if (requestLine == null) {
                    logger.warn("Client disconnected (null read)");
                    connected = false;
                    break;
                }

                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (SocketException se) {
                logger.error("Socket error, client likely disconnected", se);
                connected = false;
                break;
            } catch (IOException e) {
                logger.error("IO error during request processing", e);
                connected = false;
                break;
            }


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.warn("Worker thread interrupted", e);
                Thread.currentThread().interrupt();
            }
        }


        closeConnection();
    }

    private void closeConnection() {
        try {
            logger.info("Closing connection to client: " + connection.getInetAddress());
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (IOException e) {
            logger.error("Error closing connection", e);
        }
    }

    @Override
    public void resultsUpdated(Result result) throws TriathlonException {
        if (!connected || connection.isClosed()) {
            logger.warn("Cannot send update to disconnected client");
            throw new TriathlonException("Client is no longer connected");
        }

        Response resp = JsonProtocolUtils.createResultUpdatedResponse(result);
        logger.debug("Sending result update to client " + connection.getInetAddress() +
                " for participant: " + result.getParticipant().getName() +
                " with points: " + result.getPoints());

        try {
            sendResponse(resp);
            logger.debug("Successfully sent result update to client: " + connection.getInetAddress());
        } catch (IOException e) {
            logger.error("Failed to send result update to client: " + connection.getInetAddress(), e);
            connected = false;
            throw new TriathlonException("Sending error: " + e);
        }
    }

    private static final Response okResponse = JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request) {
        Response response = null;

        if (request.getType() == RequestType.LOGIN) {
            logger.debug("Login request..." + request.getData());
            Referee referee = gsonFormatter.fromJson(gsonFormatter.toJson(request.getData()), Referee.class);
            try {
                RefereeDiscipline asssignement = server.login(referee, this);
                return JsonProtocolUtils.createLoginResponse(asssignement);
            } catch (TriathlonException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.LOGOUT) {
            logger.debug("Logout request " + request.getData());
            Referee referee = gsonFormatter.fromJson(gsonFormatter.toJson(request.getData()), Referee.class);
            try {
                server.logout(referee, this);
                connected = false;
                return okResponse;
            } catch (TriathlonException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.GET_RESULTS_BY_EVENT) {
            logger.debug("GetResultsByEvent request " + request.getData());
            Long eventId = gsonFormatter.fromJson(gsonFormatter.toJson(request.getData()), Long.class);
            try {
                List<Result> results = server.getResultsByEvent(eventId);
                return JsonProtocolUtils.createGetResultsByEventResponse(results);
            } catch (Exception e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.UPDATE_RESULT) {
            logger.debug("UpdateResult request " + request.getData());
            Result result = gsonFormatter.fromJson(gsonFormatter.toJson(request.getData()), Result.class);
            try {
                server.updateResult(result);
                return okResponse;
            } catch (Exception e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.GET_TOTAL_POINTS_FOR_PARTICIPANT) {
            logger.debug("GetTotalPointsForParticipant request " + request.getData());
            Object[] params = gsonFormatter.fromJson(gsonFormatter.toJson(request.getData()), Object[].class);
            Long participantId = ((Number) params[0]).longValue();
            Long eventId = ((Number) params[1]).longValue();
            try {
                int points = server.getTotalPointsForParticipant(participantId, eventId);
                response = JsonProtocolUtils.createOkResponse();
                response.setTotalPoints(points);
            } catch (Exception e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException {

        if (connection.isClosed()) {
            logger.warn("Attempted to send response on closed socket");
            throw new IOException("Connection is closed");
        }

        String responseLine = gsonFormatter.toJson(response);
        logger.debug("Sending response: " + responseLine);

        synchronized (output) {
            output.println(responseLine);
            output.flush();


            if (output.checkError()) {
                logger.error("PrintWriter encountered an error after write");
                throw new IOException("Error writing to socket output stream");
            }
        }
    }
}