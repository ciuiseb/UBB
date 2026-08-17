package ppd.ticketmanager.client;

import ppd.ticketmanager.model.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client implements Runnable {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private final Random random = new Random();

    @Override
    public void run() {
        try {
            List<Integer> shows = (List<Integer>) sendRequest(0, null);
            if (shows == null || shows.isEmpty()) return;

            Integer totalSeats = (Integer) sendRequest(3, null);

            int showId = shows.get(random.nextInt(shows.size()));
            int ticketCount = random.nextInt(5) + 1;

            List<Integer> selectedSeats = getSeats(ticketCount, totalSeats);

            System.out.println("Client vrea la S" + showId + " locurile: " + selectedSeats);
            TicketRequest req = new TicketRequest(showId, ticketCount, selectedSeats);
            Object response = sendRequest(1, req);

            if (response instanceof String) {
                return;
            }
            SaleRecord reservation = (SaleRecord) response;

            Thread.sleep(200);
            String payResult = (String) sendRequest(2, reservation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object sendRequest(int command, Object payload) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeInt(command);
            if (payload != null) {
                out.writeObject(payload);
            }
            out.flush();

            return in.readObject();

        } catch (Exception e) {
            return "Network Error: " + e.getMessage();
        }
    }

    private List<Integer> getSeats(int count, int totalSeats) {
        Set<Integer> seats = new HashSet<>();
        while (seats.size() < count) {
            seats.add(random.nextInt(totalSeats) + 1);
        }
        return new ArrayList<>(seats);
    }
}