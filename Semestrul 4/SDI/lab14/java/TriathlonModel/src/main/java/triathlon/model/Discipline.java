package triathlon.model;

import java.util.Objects;

public enum Discipline {
    RUNNING(1L),
    SWIMMING(2L),
    CYCLING(3L);

    private final Long id;

    Discipline(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public static Discipline getById(Long id) {
        for (Discipline discipline : values()) {
            if (Objects.equals(discipline.getId(), id)) {
                return discipline;
            }
        }
        throw new IllegalArgumentException("No Discipline with id " + id);
    }
}
