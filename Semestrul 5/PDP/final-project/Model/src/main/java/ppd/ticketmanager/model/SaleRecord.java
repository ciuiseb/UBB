package ppd.ticketmanager.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SaleRecord implements Serializable {
    private final String saleId;
    private final LocalDateTime saleDate;
    private final int showId;
    private final int ticketCount;
    private final List<Integer> seatNumbers;
    private final double totalAmount;
    private volatile SaleStatus status;

    public SaleRecord(int showId, int ticketCount, List<Integer> seatNumbers, double totalAmount) {
        this.saleId = UUID.randomUUID().toString().substring(0, 8);
        this.saleDate = LocalDateTime.now();
        this.showId = showId;
        this.ticketCount = ticketCount;
        this.seatNumbers = seatNumbers;
        this.totalAmount = totalAmount;
        this.status = SaleStatus.RESERVED;
    }

    public String getSaleId() { return saleId; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public int getShowId() { return showId; }
    public int getTicketCount() { return ticketCount; }
    public List<Integer> getSeatNumbers() { return seatNumbers; }
    public double getTotalAmount() { return totalAmount; }
    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Vanzare{" +
                "id='" + saleId + '\'' +
                ", show=" + showId +
                ", seats=" + seatNumbers +
                ", status=" + status +
                '}';
    }
}