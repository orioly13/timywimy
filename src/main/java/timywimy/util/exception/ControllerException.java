package timywimy.util.exception;

public class ControllerException extends RestException {

    public ControllerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ControllerException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
