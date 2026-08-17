package triathlon.services.messaging;

import triathlon.model.Result;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;

import javax.jms.*;

public class ActiveMQPublisher implements MessageBrokerPublisher {
    private static final Logger logger = LogManager.getLogger(ActiveMQPublisher.class);

    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private Topic resultsTopic;
    private Gson gson;

    public ActiveMQPublisher(String brokerUrl) {
        logger.info("Initializing ActiveMQ Publisher with broker URL: {}", brokerUrl);

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            resultsTopic = session.createTopic("triathlon.results.updates");

            producer = session.createProducer(resultsTopic);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            gson = new Gson();

            logger.info("ActiveMQ Publisher initialized successfully");

        } catch (JMSException e) {
            logger.error("Failed to initialize ActiveMQ Publisher", e);
        }
    }

    @Override
    public void publishResultUpdate(Result result) {
        try {
            String resultJson = gson.toJson(result);

            TextMessage message = session.createTextMessage(resultJson);

            message.setStringProperty("messageType", "RESULT_UPDATE");
            message.setLongProperty("eventId", result.getEvent().getId());
            message.setLongProperty("participantId", result.getParticipant().getId());
            message.setLongProperty("resultId", result.getId());
            message.setLongProperty("timestamp", System.currentTimeMillis());

            producer.send(message);

            logger.debug("Published result update for result ID: {} to topic: {}",
                    result.getId(), resultsTopic.getTopicName());

        } catch (Exception e) {
            logger.error("Failed to publish result update for result ID: {}", result.getId(), e);
        }
    }

    @Override
    public void close() {
        logger.info("Closing ActiveMQ Publisher");
        try {
            if (producer != null) {
                producer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
            logger.info("ActiveMQ Publisher closed successfully");
        } catch (JMSException e) {
            logger.error("Error closing ActiveMQ Publisher", e);
        }
    }

}