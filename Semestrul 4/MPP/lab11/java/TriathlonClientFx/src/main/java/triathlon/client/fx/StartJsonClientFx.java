package triathlon.client.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import triathlon.client.fx.gui.LoginController;
import triathlon.network.jsonprotocol.TriathlonServicesJsonProxy;
import triathlon.services.ITriathlonServices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFx extends Application {
    private static final Logger logger = LogManager.getLogger(StartJsonClientFx.class);

    private static final int DEFAULT_PORT = 55555;
    private static final String DEFAULT_SERVER = "localhost";

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Starting Triathlon Client FX...");

            
            Properties clientProps = new Properties();
            try {
                clientProps.load(StartJsonClientFx.class.getResourceAsStream("/triathlon-client.properties"));
                logger.info("Loaded client properties");
            } catch (IOException e) {
                logger.error("Cannot find triathlon-client.properties", e);
                logger.info("Using default server settings: {}:{}", DEFAULT_SERVER, DEFAULT_PORT);
            }

            
            String serverIP = clientProps.getProperty("server.host", DEFAULT_SERVER);
            int serverPort = DEFAULT_PORT;
            try {
                serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
            } catch (NumberFormatException ex) {
                logger.error("Wrong port number in settings", ex);
                serverPort = DEFAULT_PORT;
            }
            logger.info("Using server {}:{}", serverIP, serverPort);

            
            ITriathlonServices server = new TriathlonServicesJsonProxy(serverIP, serverPort);

            
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Parent loginRoot = loginLoader.load();
            LoginController loginController = loginLoader.getController();

            
            loginController.setServer(server);

            
            primaryStage.setTitle("Triathlon Referee Login");
            primaryStage.setScene(new Scene(loginRoot));
            primaryStage.setResizable(false);
            primaryStage.show();

            logger.info("Triathlon Client FX started successfully");

        } catch (Exception e) {
            logger.error("Error starting application", e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}