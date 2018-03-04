package timywimy.util.exception;

public class ServiceException extends RestException {

    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ServiceException(ErrorCode errorCode, String message, Exception e) {
        super(errorCode, message, e);
    }
}
