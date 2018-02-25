package timywimy.util.exception;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    UNKNOWN(-1, "Unknown ErrorCode"),

    OK(0, null),

    INTERNAL_GENERAL(1, "Internal General Error"),

    INTERNAL_REPOSITORY(101, "Internal General Error (Repository level)"),

    INTERNAL_SERVICE(201, "Internal General Error (Service level)"),

    INTERNAL_CONTROLLER(301, "Internal General Error (Controller level)");

    private static final Map<Integer, ErrorCode> mapByCode;

    private int code;
    private String message;

    static {
        mapByCode = new HashMap<>();
        for (ErrorCode value : ErrorCode.values()) {
            mapByCode.put(value.getCode(), value);
        }
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorCode fromCode(int code) {
        ErrorCode errorCode = mapByCode.get(code);
        return errorCode == null ? ErrorCode.UNKNOWN : errorCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("[%s;%s]", code, message);
    }
}
