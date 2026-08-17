package ppd.ticketmanager.server;

import ppd.ticketmanager.model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private final Map<Integer, Show> shows;
    private final List<SaleRecord> globalSalesLog;

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduler;
    private final Object logLock = new Object();
    private final int tMaxSeconds;

    public Server(List<Show> initialShows, int poolSize, int tMaxSeconds) {
        this.shows = new ConcurrentHashMap<>();
        for (Show s : initialShows) {
            this.shows.put(s.getId(), s);
        }

        this.globalSalesLog = new ArrayList<>();
        this.tMaxSeconds = tMaxSeconds;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.scheduler = Executors.newScheduledThreadPool(initialShows.size() + 2);
    }

    public List<Integer> getAvailableShowIds() {
        return new ArrayList<>(shows.keySet());
    }

    public Integer getNumberOfSeats() {
        if (shows.isEmpty()) return 0;
        return shows.values().iterator().next().getSeats().size();
    }

    public Future<SaleRecord> reserve(TicketRequest request) {
        return executorService.submit(() -> {
            Show show = shows.get(request.getShowId());
            if (show == null) throw new IllegalArgumentException("ID Spectacol invalid!");

            synchronized (show.lock) {
                List<Integer> wanted = request.getPreferredSeats();

                if (!areSeatsAvailable(show, wanted)) {
                    throw new RuntimeException("Locuri indisponibile la " + show);
                }

                double total = 0;
                for (int id : wanted) {
                    show.getSeats().get(id).setStatus(SeatStatus.RESERVED);
                    total += show.getPrice();
                }

                SaleRecord sale = new SaleRecord(show.getId(), wanted.size(), wanted, total);

                synchronized (logLock) {
                    globalSalesLog.add(sale);
                }

                startExpirationTimer(sale, show);
                return sale;
            }
        });
    }

    public Future<String> processPayment(SaleRecord saleCopyFromNetwork) {
        return executorService.submit(() -> {
            SaleRecord actualRecord = null;
            synchronized (logLock) {
                for (SaleRecord s : globalSalesLog) {
                    if (s.getSaleId().equals(saleCopyFromNetwork.getSaleId())) {
                        actualRecord = s;
                        break;
                    }
                }
            }
            if (actualRecord == null) {
                return "ESEC: Vanzarea nu a fost gasita in sistem!";
            }
            Show show = shows.get(actualRecord.getShowId());
            synchronized (show.lock) {
                if (actualRecord.getStatus() == SaleStatus.CANCELLED) {
                    return "ESEC: Timpul a expirat! Rezervare anulata.";
                }
                if (actualRecord.getStatus() == SaleStatus.PAID) {
                    return "INFO: Deja platit.";
                }
                actualRecord.setStatus(SaleStatus.PAID);
                for (int id : actualRecord.getSeatNumbers()) {
                    show.getSeats().get(id).setStatus(SeatStatus.SOLD);
                }
                show.addToBalance(actualRecord.getTotalAmount());

                System.out.println("[Server] Plata confirmata pentru ID: " + actualRecord.getSaleId());
                return "SUCCES";
            }
        });
    }

    public void startReporter(int intervalSeconds) {
        scheduler.scheduleAtFixedRate(() -> {
            StringBuilder report = new StringBuilder();
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            for (Show show : shows.values()) {
                synchronized (show.lock) {
                    double calculatedBalance = 0;
                    List<String> paidSaleIds = new ArrayList<>();
                    Set<Integer> seatsSoldInLog = new HashSet<>();
                    List<String> errors = new ArrayList<>();

                    synchronized (logLock) {
                        for (SaleRecord s : globalSalesLog) {
                            if (s.getShowId() == show.getId() && s.getStatus() == SaleStatus.PAID) {
                                double expectedTotal = s.getTicketCount() * show.getPrice();
                                if (Math.abs(expectedTotal - s.getTotalAmount()) > 0.01) {
                                    errors.add("Eroare suma vanzare " + s.getSaleId());
                                }

                                calculatedBalance += s.getTotalAmount();
                                paidSaleIds.add(s.getSaleId());
                                seatsSoldInLog.addAll(s.getSeatNumbers());
                            }
                        }
                    }
                    if (Math.abs(calculatedBalance - show.getBalance()) > 0.01) {
                        errors.add("Sold incorect (Real=" + show.getBalance() + ", Calc=" + calculatedBalance + ")");
                    }

                    for (Integer seatId : seatsSoldInLog) {
                        Seat seat = show.getSeats().get(seatId);
                        if (seat.getStatus() != SeatStatus.SOLD) {
                            errors.add("Locul " + seatId + " e platit in log dar nu e SOLD in sala");
                        }
                    }

                    for (Seat seat : show.getSeats().values()) {
                        if (seat.getStatus() == SeatStatus.SOLD) {
                            if (!seatsSoldInLog.contains(seat.getNumber())) {
                                errors.add("Locul " + seat.getNumber() + " e SOLD in sala dar fara vanzare in log");
                            }
                        }
                    }

                    boolean isCorrect = errors.isEmpty();
                    String statusText = isCorrect ? "CORECT" : "INCORECT (" + String.join(", ", errors) + ")";


                    report.append(String.format("%s | %s | Sold: %.2f | Vanzari: %s | %s\n",
                            timestamp,
                            show.toString(),
                            show.getBalance(),
                            paidSaleIds.toString(),
                            statusText
                    ));
                }
            }
            report.append("------------------------------------------------------------\n");

            logToFile(report.toString(), "files/report.txt");

        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    private boolean areSeatsAvailable(Show show, List<Integer> ids) {
        for (int id : ids) {
            Seat s = show.getSeats().get(id);
            if (s == null || s.getStatus() != SeatStatus.FREE) return false;
        }
        return true;
    }

    private void startExpirationTimer(SaleRecord sale, Show show) {
        scheduler.schedule(() -> {
            synchronized (show.lock) {
                if (sale.getStatus() == SaleStatus.RESERVED) {
                    sale.setStatus(SaleStatus.CANCELLED);
                    for (int id : sale.getSeatNumbers()) {
                        if (show.getSeats().get(id).getStatus() == SeatStatus.RESERVED)
                            show.getSeats().get(id).setStatus(SeatStatus.FREE);
                    }
                }
            }
        }, tMaxSeconds, TimeUnit.SECONDS);
    }

    private void logToFile(String s, String file) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(s + "\n");
        } catch (Exception ignored) {
        }
    }

    public void stop() {
        executorService.shutdown();
        scheduler.shutdown();
    }
}