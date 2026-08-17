package triathlon.rest.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import triathlon.persistence.impl.EventRepositoryImpl;
import triathlon.persistence.interfaces.EventRepository;

@Configuration
public class RepositoryConfig {

    @Bean
    public EventRepository eventRepository() {
        return new EventRepositoryImpl();
    }
}