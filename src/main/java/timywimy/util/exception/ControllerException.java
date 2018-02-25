package timywimy.util.exception;

public class ControllerException extends APIException {

    public ControllerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ControllerException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }
}
