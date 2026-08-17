package triathlon.services.messaging;

import triathlon.model.Result;

public interface MessageBrokerPublisher {
    void publishResultUpdate(Result result);
    void close();
}
