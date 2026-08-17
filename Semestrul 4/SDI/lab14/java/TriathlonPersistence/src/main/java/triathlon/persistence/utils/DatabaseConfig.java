package triathlon.persistence.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static DatabaseConfig instance;

    private DatabaseConfig() {}

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    private static Connection connection;

    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();

            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("Unable to find config.properties in the resources directory");
                }
                props.load(input);
            }
            String dbUrl = props.getProperty("db.url");
            if (dbUrl == null) {
                throw new IOException("db.url property not found in config.properties");
            }

            connection = DriverManager.getConnection(dbUrl);
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
