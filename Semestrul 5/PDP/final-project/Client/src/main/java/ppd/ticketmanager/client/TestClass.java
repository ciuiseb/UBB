package ppd.ticketmanager.client;

import ppd.ticketmanager.server.StartServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestClass {
    // Contoare pentru performanță
    public static AtomicInteger TOTAL_REQUESTS = new AtomicInteger(0);
    public static AtomicLong TOTAL_LATENCY_MS = new AtomicLong(0); // Suma timpilor de răspuns

    public static void main(String[] args) {
        Properties props = loadProperties("files/config.txt");

        String[] serverArgs = new String[]{
                props.getProperty("server.port", "5000"),
                props.getProperty("server.shows_file", "files/spectacole.txt"),
                props.getProperty("server.pool_size", "10"), // <--- SCHIMBĂ AICI (1 sau 10)
                props.getProperty("server.t_max", "10"),
                props.getProperty("server.audit_interval", "5")
        };

        int CLIENT_COUNT = Integer.parseInt(props.getProperty("client.count", "10"));
        long testDurationSeconds = Long.parseLong(props.getProperty("test.running-time", "60")); // Putem testa 60 secunde
        long durationMs = testDurationSeconds * 1000;

        Thread serverThread = new Thread(() -> StartServer.main(serverArgs));
        serverThread.start();

        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        System.out.println("=== START TEST (Standard): " + CLIENT_COUNT + " Clienti, pauza 2s ===");

        ExecutorService clientPool = Executors.newFixedThreadPool(CLIENT_COUNT);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < CLIENT_COUNT; i++) {
            clientPool.submit(() -> {
                while (System.currentTimeMillis() - startTime < durationMs) {
                    long reqStart = System.currentTimeMillis();

                    new Client().run();

                    long reqEnd = System.currentTimeMillis();

                    TOTAL_REQUESTS.incrementAndGet();
                    TOTAL_LATENCY_MS.addAndGet(reqEnd - reqStart);

                    try { Thread.sleep(2000); } catch (InterruptedException e) { break; }
                }
            });
        }

        try { Thread.sleep(durationMs + 2000); } catch (InterruptedException e) {}
        clientPool.shutdownNow();


        int totalOps = TOTAL_REQUESTS.get();
        long totalTime = TOTAL_LATENCY_MS.get();
        double avgLatency = (totalOps > 0) ? (double) totalTime / totalOps : 0;

        System.out.println("\n=== Rezultate ===");
        System.out.println("Pool Size Server: " + props.getProperty("server.pool_size"));
        System.out.println("Cereri Totale: " + totalOps);
        System.out.println("Timp Mediu : " + String.format("%.2f", avgLatency) + " ms");
        System.out.println("=================\n");

        System.exit(0);
    }

    private static Properties loadProperties(String filename) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(filename)) { props.load(fis); }
        catch (IOException e) { }
        return props;
    }
}