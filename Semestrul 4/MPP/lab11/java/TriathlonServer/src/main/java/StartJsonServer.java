import server.ServiceImpl;
import triathlon.network.jsonprotocol.TriathlonClientJsonWorker;
import triathlon.persistence.impl.*;
import triathlon.services.ITriathlonServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartJsonServer {
    private static final Logger logger = LogManager.getLogger(StartJsonServer.class);
    private static final int DEFAULT_PORT = 55555;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        var eventRepository = new EventRepositoryHibernateImpl();
        var participantrepository = new ParticipantRepositoryHibernateImpl();
        var resultrepository = new ResultRepositoryImpl(eventRepository, participantrepository);
        var refereerepository = new RefereeRepositoryImpl();
        var refereeDisciplineRepository = new RefereeDisciplineRepositoryImpl(refereerepository, eventRepository);

        ITriathlonServices serverImpl = new ServiceImpl(eventRepository, participantrepository, refereerepository, resultrepository, refereeDisciplineRepository);
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartJsonServer.class.getResourceAsStream("/config.properties"));
            logger.info("Loaded client properties");
        } catch (IOException e) {
            logger.error("Cannot find triathlon-client.properties", e);
        }
        int port = serverProps.getProperty("triathlon.server.port") != null ? Integer.parseInt(serverProps.getProperty("triathlon.server.port")) : DEFAULT_PORT;
        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid port number", e);
            System.out.println("Invalid port number. Using default port " + DEFAULT_PORT);
        }

        logger.info("Starting server on port: " + port);
        System.out.println("Starting server on port: " + port);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started. Waiting for clients...");
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Client connected from: " + clientSocket.getInetAddress());
                    System.out.println("Client connected from: " + clientSocket.getInetAddress());

                    TriathlonClientJsonWorker worker = new TriathlonClientJsonWorker(serverImpl, clientSocket);
                    executorService.submit(worker);
                } catch (IOException e) {
                    logger.error("Error accepting client", e);
                    System.out.println("Error accepting client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Error starting server", e);
            System.out.println("Error starting server: " + e.getMessage());
        } finally {
            executorService.shutdown();
            logger.info("Server stopped");
            System.out.println("Server stopped");
        }
    }
}