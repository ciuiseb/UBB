package ppd.ticketmanager.model;

import ppd.ticketmanager.model.Seat;
import ppd.ticketmanager.model.SeatStatus;

import java.util.HashMap;
import java.util.Map;

public class Show {
    private final int id;
    private final String title;
    private final String date;
    private final double price;
    private final Map<Integer, Seat> seats;
    private double currentBalance;

    public final Object lock = new Object();

    public Show(int id, String title, String date, double price, int numberOfSeats) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.price = price;
        this.currentBalance = 0.0;
        this.seats = new HashMap<>();

        for (int i = 1; i <= numberOfSeats; i++) {
            seats.put(i, new Seat(i, price));
        }
    }

    public int getId() { return id; }
    public double getPrice() { return price; }
    public Map<Integer, Seat> getSeats() { return seats; }

    public void addToBalance(double amount) { this.currentBalance += amount; }
    public double getBalance() { return currentBalance; }

    @Override
    public String toString() {
        return title + " (" + date + ")";
    }
}