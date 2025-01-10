package ubb.scs.map.domain.entities;

import java.time.LocalDate;
import java.util.Date;

public class Friendship extends Tuple<Long> {
    private LocalDate friendsSince;
    public Friendship(Long  first, Long  second) {
        super(first, second);
        friendsSince = LocalDate.now();
    }
    public Friendship(Long  first, Long  second, LocalDate date) {
        super(first, second);
        friendsSince = date;
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return getFirst().equals(getSecond());
    }


    @Override
    public String toString() {
        return "Friendship{" +
                "first = " + super.getFirst() +
                "; second = " + super.getSecond() +
                "}";
    }

    public LocalDate getFriendsSince() {
        return friendsSince;
    }
}
