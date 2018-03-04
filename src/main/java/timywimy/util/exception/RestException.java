package timywimy.util.exception;

public class RestException extends RuntimeException {
    protected ErrorCode errorCode;
//    protected String message;

    public RestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, String message, Exception e) {
        super(message,e);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
