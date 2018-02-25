package timywimy.util.exception;

public class RepositoryException extends APIException {

    public RepositoryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RepositoryException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
