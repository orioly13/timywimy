package timywimy.util.exception;

public class ServiceException extends APIException {
    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
