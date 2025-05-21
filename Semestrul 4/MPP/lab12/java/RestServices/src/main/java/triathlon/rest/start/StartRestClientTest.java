package triathlon.rest.start;

import triathlon.rest.client.EventsClient;
import triathlon.rest.services.ServiceException;
import org.springframework.web.client.RestClientException;
import triathlon.model.Event;

import java.util.concurrent.atomic.AtomicReference;

public class StartRestClientTest {
    private final static EventsClient eventsClient = new EventsClient();
    private static AtomicReference<Long> idCreated = new AtomicReference<>();

    public static void main(String[] args) {
        try {
            testCreate();
            testGetAll();
            testGetById();
            testUpdate();
            testDelete();
        } catch(RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void testCreate() {
        Event eventTest = new Event(
                "Test test",
                "some date",
                "some location",
                "no description"
        );

        System.out.println("Adding a new event " + eventTest);
        show(() -> {
            Event created = eventsClient.create(eventTest);
            System.out.println("Created: " + created.getId() + " - " + created.getName());
            idCreated.set(created.getId());
            return created;
        });
    }

    private static void testGetAll() {
        System.out.println("\nPrinting all events ...");
        show(() -> {
            Event[] res = eventsClient.getAll();
            for(Event e : res) {
                System.out.println(e.getId() + ": " + e.getName() + " - " + e.getDate() + " - " + e.getLocation());
            }
            return null;
        });
    }

    private static void testGetById() {
        System.out.println("\nInfo for event with id=" + idCreated.get());
        show(() -> {
            Event event = eventsClient.getById(idCreated.get());
            System.out.println(event.getId() + ": " + event.getName() +
                    "\nDate: " + event.getDate() +
                    "\nLocation: " + event.getLocation() +
                    "\nDescription: " + event.getDescription());
            return event;
        });
    }

    private static void testUpdate() {
        System.out.println("\nUpdating event with id=" + idCreated.get());
        show(() -> {
            Event event = eventsClient.getById(6L);

            event.setName("Updated Event Name");
            event.setDescription("This description has been updated");

            Event updated = eventsClient.update(event);
            System.out.println("Updated: " + updated.getId() + " - " + updated.getName() +
                    "\nNew description: " + updated.getDescription());
            return updated;
        });
    }

    private static void testDelete() {
        System.out.println("\nDeleting event with id=" + idCreated.get());
        show(() -> {
            eventsClient.delete(idCreated.get());
            return null;
        });
    }

    private static <T> T show(Callable<T> task) {
        try {
            return task.call();
        } catch (ServiceException e) {
            System.out.println("Service exception: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("Caused by: " + cause.getMessage());
            }
            return null;
        }
    }

    interface Callable<T> {
        T call();
    }
}