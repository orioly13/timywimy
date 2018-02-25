package timywimy.util.exception;

public class APIException extends RuntimeException {
    protected ErrorCode errorCode;

    public APIException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public APIException(ErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
