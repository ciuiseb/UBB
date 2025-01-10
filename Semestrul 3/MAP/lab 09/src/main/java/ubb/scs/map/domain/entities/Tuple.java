package ubb.scs.map.domain.entities;

import java.util.Objects;

public class Tuple<ID> extends Entity<ID> {
    private ID first;
    private ID second;

    public Tuple(ID first, ID second) {
        this.first = first;
        this.second = second;
    }

    public ID getFirst() {
        return first;
    }

    public ID getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?> that = (Tuple<?>) o;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}