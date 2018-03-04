package timywimy.util.exception;

public class RestException extends RuntimeException {
    protected ErrorCode errorCode;
    protected String message;

    public RestException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public RestException(ErrorCode errorCode, String message, Exception e) {
        super(e);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
