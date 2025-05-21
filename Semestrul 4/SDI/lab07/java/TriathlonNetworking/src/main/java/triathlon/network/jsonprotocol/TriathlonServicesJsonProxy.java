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

            
            readerThread = new ReaderThread();
            readerThread.setDaemon(true); 
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

        
        String requestJson = gsonFormatter.toJson(request);
        logger.info("Sending request: " + requestJson);

        synchronized (output) {
            output.println(requestJson);
            output.flush();
        }

    }

    private Response readResponse() throws TriathlonException {
        try {
            
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

    
    private class ReaderThread extends Thread {
        @Override
        public void run() {
            logger.info("Reader thread started");
            try {
                while (!finished && !Thread.currentThread().isInterrupted()) {
                    
                    if (connection != null && !connection.isClosed()) {
                        try {
                            
                            if (input.ready()) {
                                String responseJson = input.readLine();
                                logger.fine("Reader thread read: " + responseJson);

                                
                                if (responseJson == null) {
                                    logger.warning("Received null response, server likely disconnected");
                                    throw new IOException("Server disconnected");
                                }

                                
                                Response response = gsonFormatter.fromJson(responseJson, Response.class);

                                
                                if (response.getType() == ResponseType.RESULT_UPDATED) {
                                    handleResultUpdated(response);
                                } else {
                                    
                                    responses.put(response);
                                }
                            } else {
                                
                                Thread.sleep(10);
                            }
                        } catch (InterruptedException e) {
                            logger.warning("Reader thread interrupted");
                            Thread.currentThread().interrupt();
                            break;
                        } catch (IOException e) {
                            logger.severe("IO error in reader thread: " + e.getMessage());
                            
                            if (!finished) {
                                finished = true;
                                break;
                            }
                        }
                    } else {
                        
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