package triathlon.client.fx.messaging;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.model.Result;
import triathlon.client.fx.gui.RefereeDashboardController;

import javax.jms.*;

public class MessageBrokerConsumer implements AutoCloseable{
    private static final Logger logger = LogManager.getLogger(MessageBrokerConsumer.class);

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private Topic resultsTopic;
    private Gson gson;

    private RefereeDashboardController dashboard;

    public MessageBrokerConsumer(String brokerUrl) throws JMSException {
        logger.info("Initializing ActiveMQ Consumer with broker URL: {}", brokerUrl);

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            resultsTopic = session.createTopic("triathlon.results.updates");
            consumer = session.createConsumer(resultsTopic);
            consumer.setMessageListener(this::onMessageReceived);
            gson = new Gson();
            connection.start();
            logger.info("ActiveMQ Consumer initialized and started successfully");

        } catch (JMSException e) {
            logger.error("Failed to initialize ActiveMQ Consumer", e);
            throw e;
        }
    }

    public void setDashboard(RefereeDashboardController dashboard) {
        this.dashboard = dashboard;
        logger.debug("Dashboard set for message broker consumer");
    }

    private void onMessageReceived(Message message) {
        try {
            if (!(message instanceof TextMessage)) {
                logger.warn("Received non-text message: {}", message.getClass().getSimpleName());
                return;
            }

            TextMessage textMessage = (TextMessage) message;
            String messageType = textMessage.getStringProperty("messageType");

            if (!"RESULT_UPDATE".equals(messageType)) {
                logger.debug("Ignoring message with type: {}", messageType);
                return;
            }

            String jsonContent = textMessage.getText();
            logger.debug("Received result update message: {}", jsonContent);

            Result result = gson.fromJson(jsonContent, Result.class);

            if (dashboard != null) {
                dashboard.handleResultUpdate(result);
            }

        } catch (Exception e) {
            logger.error("Error processing received message", e);
        }
    }

    public void close() {
        logger.info("Closing ActiveMQ Consumer");

        try {
            if (consumer != null) {
                consumer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
            logger.info("ActiveMQ Consumer closed successfully");
        } catch (JMSException e) {
            logger.error("Error closing ActiveMQ Consumer", e);
        }
    }
}