package timywimy.util.exception;

public class ControllerException extends RestException {

    public ControllerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ControllerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ControllerException(ErrorCode errorCode, String message, Exception e) {
        super(errorCode, message, e);
    }
}
