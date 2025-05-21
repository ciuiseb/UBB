package triathlon.rest.client;

import triathlon.model.Event;
import triathlon.rest.services.ServiceException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class EventsClient {
    RestClient restClient = RestClient.builder()
            .requestInterceptor(new CustomRestClientInterceptor())
            .build();

    public static final String URL = "http://localhost:8080/api/events";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Event[] getAll() {
        return execute(() -> restClient.get()
                .uri(URL)
                .retrieve()
                .body(Event[].class));
    }

    public Event getById(Long id) {
        return execute(() -> restClient.get()
                .uri(String.format("%s/%d", URL, id))
                .retrieve()
                .body(Event.class));
    }

    public Event[] getByRefereeId(Long refereeId) {
        return execute(() -> restClient.get()
                .uri(String.format("%s/referee/%d", URL, refereeId))
                .retrieve()
                .body(Event[].class));
    }

    public Event create(Event event) {
        return execute(() -> restClient.post()
                .uri(URL)
                .contentType(APPLICATION_JSON)
                .body(event)
                .retrieve()
                .body(Event.class));
    }

    public Event update(Event event) {
        return execute(() -> {
            return restClient.put()
                    .uri(String.format("%s/%d", URL, event.getId()))
                    .contentType(APPLICATION_JSON)
                    .body(event)
                    .retrieve()
                    .body(Event.class);
        });
    }

    public void delete(Long id) {
        execute(() -> {
            restClient.delete()
                    .uri(String.format("%s/%d", URL, id))
                    .retrieve()
                    .toBodilessEntity();
            return null;
        });
    }

    public class CustomRestClientInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending a " + request.getMethod() +
                    " request to " + request.getURI() +
                    " and body [" + new String(body) + "]");

            ClientHttpResponse response = null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code " + response.getStatusCode());
            } catch(IOException ex) {
                System.err.println("Error during execution: " + ex);
            }
            return response;
        }
    }
}