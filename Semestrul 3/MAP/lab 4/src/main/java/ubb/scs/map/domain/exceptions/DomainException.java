package ubb.scs.map.domain.exceptions;

public class DomainException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public DomainException(String message) {
        super(message);
    }
}
