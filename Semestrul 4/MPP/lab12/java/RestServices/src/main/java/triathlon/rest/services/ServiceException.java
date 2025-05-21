package triathlon.rest.services;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Exception e) {
        super(e);
    }
}
