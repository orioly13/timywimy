package timywimy.util.exception;

public class RepositoryException extends RestException {

    public RepositoryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RepositoryException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public RepositoryException(ErrorCode errorCode, String message, Exception e) {
        super(errorCode, message, e);
    }
}
