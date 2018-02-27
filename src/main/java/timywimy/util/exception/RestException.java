package timywimy.util.exception;

public class RestException extends RuntimeException {
    protected ErrorCode errorCode;

    public RestException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
