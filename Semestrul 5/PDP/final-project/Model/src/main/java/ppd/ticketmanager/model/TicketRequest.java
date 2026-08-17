package ppd.ticketmanager.model;
import java.io.Serializable;
import java.util.List;

public class TicketRequest implements Serializable {
    private final int showId;
    private final int numberOfTickets;
    private final List<Integer> preferredSeats;

    public TicketRequest(int showId, int numberOfTickets, List<Integer> preferredSeats) {
        this.showId = showId;
        this.numberOfTickets = numberOfTickets;
        this.preferredSeats = preferredSeats;
    }

    public int getShowId() { return showId; }
    public int getNumberOfTickets() { return numberOfTickets; }
    public List<Integer> getPreferredSeats() { return preferredSeats; }
}