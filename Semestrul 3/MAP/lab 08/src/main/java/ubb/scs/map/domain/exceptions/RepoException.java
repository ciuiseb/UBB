package ubb.scs.map.domain.exceptions;

public class RepoException extends RuntimeException {
    /*  Constructor for the RepoException class
        Input: message - a string
        Output: an instance of the RepoException class
        The constructor calls the constructor of the RuntimeException class with the message parameter
     */
    public RepoException(String message) {
        super(message);
    }
    public RepoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepoException(Throwable cause) {
        super(cause);
    }

    public RepoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
