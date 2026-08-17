package ppd.ticketmanager.server;

import ppd.ticketmanager.model.Show;
import ppd.ticketmanager.model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Future;

public class StartServer {
    private static int PORT;
    private static String SHOWS_FILE;
    private static int WORKER_POOL_SIZE;
    private static int T_MAX;
    private static int AUDIT_INTERVAL;

    public static void main(String[] args) {
        if (args.length >= 5) {
            try {
                PORT = Integer.parseInt(args[0]);
                SHOWS_FILE = args[1];
                WORKER_POOL_SIZE = Integer.parseInt(args[2]);
                T_MAX = Integer.parseInt(args[3]);
                AUDIT_INTERVAL = Integer.parseInt(args[4]);

                System.out.println("[StartServer] Configurare incarcata din argumente:");
                System.out.printf("Port=%d, File=%s, Pool=%d, Tmax=%d, Audit=%d\n",
                        PORT, SHOWS_FILE, WORKER_POOL_SIZE, T_MAX, AUDIT_INTERVAL);
            } catch (NumberFormatException e) {
                System.err.println("[StartServer] Eroare la parsarea argumentelor. Se folosesc valorile default.");
            }
        }

        List<Show> shows = loadShowsFromFile(SHOWS_FILE);

        if (shows.isEmpty()) {
            System.err.println("Eroare: Nu s-au putut încărca spectacolele din " + SHOWS_FILE);
            return;
        }

        Server businessServer = new Server(shows, WORKER_POOL_SIZE, T_MAX);
        businessServer.startReporter(AUDIT_INTERVAL);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[StartServer] Server pornit pe portul " + PORT);
            System.out.println("[StartServer] S-au incarcat " + shows.size() + " spectacole.");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, businessServer)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            businessServer.stop();
        }
    }

    private static List<Show> loadShowsFromFile(String filename) {
        List<Show> loadedShows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String date = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());
                    int seats = Integer.parseInt(parts[4].trim());
                    loadedShows.add(new Show(id, title, date, price, seats));
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la citirea fișierului spectacole: " + e.getMessage());
        }
        return loadedShows;
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            int command = in.readInt();

            switch (command) {
                case 0:
                    List<Integer> shows = server.getAvailableShowIds();
                    out.writeObject(shows);
                    break;
                case 1:
                    TicketRequest req = (TicketRequest) in.readObject();
                    Future<SaleRecord> futureRes = server.reserve(req);
                    try {
                        SaleRecord result = futureRes.get();
                        out.writeObject(result);
                    } catch (Exception e) {
                        String errorMsg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                        out.writeObject("EROARE: " + errorMsg);
                    }
                    break;

                case 2:
                    SaleRecord sale = (SaleRecord) in.readObject();
                    Future<String> futurePay = server.processPayment(sale);
                    String payRes = futurePay.get();
                    out.writeObject(payRes);
                    break;
                case 3:
                    int totalSeats = server.getNumberOfSeats();
                    out.writeObject(totalSeats);
                    break;
            }
            out.flush();

        }  catch (Exception e) {
            System.err.println("Eroare client: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}