package timywimy.util.exception;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    UNKNOWN(-1, "Unknown ErrorCode"),
    OK(0, null),
    INTERNAL_GENERAL(1, "Internal General Error"),

    REQUEST_VALIDATION_EMPTY_FIELDS(2, "Not enough fields provided"),
    REQUEST_VALIDATION_INVALID_FIELDS(3, "Some fields are invalid"),
    REQUEST_VALIDATION_NOT_ENOUGH_RIGHTS(4, "Not enough rights for operation"),
    REQUEST_VALIDATION_SESSION_REQUIRED(5, "Session is closed or expired, please open a new one"),
    REQUEST_VALIDATION_OPERATION_RESTRICTED(6, "Operation is not allowed"),

    REGISTER_FAILED_TO_PERSIST(10, "Failed to persist user"),
    USER_ALREADY_REGISTERED(11, "User with this email already registered"),

    USER_NOT_FOUND(13, "User with provided credentials not found"),
    USER_NOT_ACTIVE(14, "User with provided credentials is not active, check your email to activate it"),
    USER_IS_BANNED(15, "User with provided credentials is banned"),
    ENTITY_NOT_FOUND(20, "Entity with this id not found"),

    INTERNAL_REPOSITORY(101, "Internal General Error (Repository level)"),
    //
    INTERNAL_SERVICE(201, "Internal General Error (Service level)"),
    //
    INTERNAL_CONTROLLER(301, "Internal General Error (Controller level)"),


    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found"),
    NOT_ALLOWED(405, "Method not allowed");


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
