package ppd.ticketmanager.model;

public class Seat {
    private final int number;
    private final double price;
    private SeatStatus status;

    public Seat(int number, double price) {
        this.number = number;
        this.price = price;
        this.status = SeatStatus.FREE;
    }

    public int getNumber() { return number; }
    public double getPrice() { return price; }

    public synchronized SeatStatus getStatus() { return status; }
    public synchronized void setStatus(SeatStatus status) { this.status = status; }

    @Override
    public String toString() { return "Loc " + number + " [" + status + "]"; }
}