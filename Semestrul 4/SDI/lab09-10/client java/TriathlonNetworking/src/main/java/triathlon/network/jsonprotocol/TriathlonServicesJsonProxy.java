package triathlon.network.jsonprotocol;

import triathlon.model.Referee;
import triathlon.model.RefereeDiscipline;
import triathlon.model.Result;
import triathlon.services.ITriathlonObserver;
import triathlon.services.ITriathlonServices;
import triathlon.services.TriathlonException;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class TriathlonServicesJsonProxy implements ITriathlonServices {
    private String host;
    private int port;

    private ITriathlonObserver client;

    private Socket connection;
    private PrintWriter output;
    private BufferedReader input;
    private Gson gsonFormatter;
    private BlockingQueue<Response> responses;
    private volatile boolean finished;
    private ReaderThread readerThread;

    private final static Logger logger = Logger.getLogger(TriathlonServicesJsonProxy.class.getName());

    public TriathlonServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
        gsonFormatter = new Gson();
    }

    public void closeConnection() {
        finished = true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
            if (readerThread != null) readerThread.interrupt();
            client = null;
        } catch (IOException e) {
            logger.warning("Error closing connection: " + e.getMessage());
        }
    }

    private void initializeConnection() throws TriathlonException {
        try {
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;

            // Create and start reader thread
            readerThread = new ReaderThread();
            readerThread.setDaemon(true); // Make it a daemon so it doesn't block application shutdown
            readerThread.start();

        } catch (IOException e) {
            logger.severe("Error connecting to server: " + e.getMessage());
            throw new TriathlonException("Error connecting to server: " + e.getMessage());
        }
    }

    private void sendRequest(Request request) throws TriathlonException {
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }

        // Serialize and send request
        String requestJson = gsonFormatter.toJson(request);
        logger.info("Sending request: " + requestJson);

        synchronized (output) {
            output.println(requestJson);
            output.flush();
        }

    }

    private Response readResponse() throws TriathlonException {
        try {
            // Wait for a response from the queue (filled by reader thread)
            Response response = responses.take();
            logger.info("Received response: " + gsonFormatter.toJson(response));

            if (response.getType() == ResponseType.ERROR) {
                logger.warning("Server error: " + response.getErrorMessage());
                throw new TriathlonException("Server error: " + response.getErrorMessage());
            }

            return response;
        } catch (InterruptedException e) {
            logger.warning("Interrupted while waiting for response: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new TriathlonException("Interrupted while waiting for response");
        }
    }

    @Override
    public RefereeDiscipline login(Referee referee, ITriathlonObserver client) throws TriathlonException {
        initializeConnection();
        this.client = client;
        Request request = JsonProtocolUtils.createLoginRequest(referee);
        sendRequest(request);
        Response response = readResponse();
        return response.getRefereeDiscipline();
    }

    @Override
    public void logout(Referee referee, ITriathlonObserver client) throws TriathlonException {
        Request request = JsonProtocolUtils.createLogoutRequest(referee);
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
    }

    @Override
    public List<Result> getResultsByEvent(Long eventId) throws TriathlonException {
        Request request = JsonProtocolUtils.createGetResultsByEventRequest(eventId);
        sendRequest(request);
        Response response = readResponse();
        return response.getResults();
    }

    @Override
    public void updateResult(Result result) throws TriathlonException {
        Request request = JsonProtocolUtils.createUpdateResultRequest(result);
        sendRequest(request);
        Response response = readResponse();
    }

    @Override
    public int getTotalPointsForParticipant(Long participantId, Long eventId) throws TriathlonException {
        Request request = JsonProtocolUtils.createGetTotalPointsForParticipantRequest(participantId, eventId);
        sendRequest(request);
        Response response = readResponse();
        return response.getTotalPoints();
    }

    // Reader thread that continuously reads from the socket and handles responses
    private class ReaderThread extends Thread {
        @Override
        public void run() {
            logger.info("Reader thread started");
            try {
                while (!finished && !Thread.currentThread().isInterrupted()) {
                    // Only try to read if connection is valid
                    if (connection != null && !connection.isClosed()) {
                        try {
                            // Check if there's data available for reading
                            if (input.ready()) {
                                String responseJson = input.readLine();
                                logger.fine("Reader thread read: " + responseJson);

                                // Check for null or empty response (indicates connection closed)
                                if (responseJson == null) {
                                    logger.warning("Received null response, server likely disconnected");
                                    throw new IOException("Server disconnected");
                                }

                                // Parse and process the response
                                Response response = gsonFormatter.fromJson(responseJson, Response.class);

                                // Handle result updated notifications
                                if (response.getType() == ResponseType.RESULT_UPDATED) {
                                    handleResultUpdated(response);
                                } else {
                                    // For regular responses, put in the queue for the main thread
                                    responses.put(response);
                                }
                            } else {
                                // No data yet, short pause to avoid busy waiting
                                Thread.sleep(10);
                            }
                        } catch (InterruptedException e) {
                            logger.warning("Reader thread interrupted");
                            Thread.currentThread().interrupt();
                            break;
                        } catch (IOException e) {
                            logger.severe("IO error in reader thread: " + e.getMessage());
                            // Only break loop if connection is supposed to be active
                            if (!finished) {
                                finished = true;
                                break;
                            }
                        }
                    } else {
                        // Connection not available, wait a bit
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                logger.severe("Unexpected error in reader thread: " + e.getMessage());
                e.printStackTrace();
            } finally {
                logger.info("Reader thread exiting");
            }
        }

        private void handleResultUpdated(Response response) {
            // Make sure we have a client to notify and that the response contains a result
            if (client != null && response.getResults() != null) {
                try {
                    logger.info("Notifying client about result update: " +
                            response.getResults().getFirst().getParticipant().getName() + " - " +
                            response.getResults().getFirst().getPoints() + " points");

                    client.resultsUpdated(response.getResults().getFirst());
                } catch (TriathlonException e) {
                    logger.warning("Error notifying client: " + e.getMessage());
                }
            } else {
                logger.warning("Cannot handle result update: client=" + client +
                        ", result=" + (response.getResults().getFirst() != null ? "present" : "null"));
            }
        }
    }
}