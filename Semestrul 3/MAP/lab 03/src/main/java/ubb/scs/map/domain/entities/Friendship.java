package ubb.scs.map.domain.entities;

public class Friendship extends SameClassPair<Long> {
    public Friendship(Long  first, Long  second) {
        super(first, second);
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
}
