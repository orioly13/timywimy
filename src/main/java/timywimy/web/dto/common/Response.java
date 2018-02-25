package timywimy.web.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import timywimy.util.exception.ErrorCode;

@JsonPropertyOrder(value = {"request_id","code","message","body"})
public class Response<T> {
    @JsonProperty("request_id")
    private int requestId;
    private int code;
    private String message;
    private T body;

    public Response() {
        this.requestId = 0;
        this.code = ErrorCode.OK.getCode();
        this.message = null;
        this.body = null;
    }

    public Response(int requestId, T body) {
        this.requestId = requestId;
        this.code = ErrorCode.OK.getCode();
        this.message = ErrorCode.OK.getMessage();
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
