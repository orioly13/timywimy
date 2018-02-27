package timywimy.web.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import timywimy.util.exception.ErrorCode;

@JsonPropertyOrder(value = {"request_id", "code", "message", "response"})
public class Response<T> {
    @JsonProperty("request_id")
    private Integer requestId;
    private Integer code;
    private String message;
    private T response;

    public Response(Integer requestId, T response) {
        this.requestId = requestId;
        this.code = ErrorCode.OK.getCode();
        this.message = ErrorCode.OK.getMessage();
        this.response = response;
    }

    public Response(Integer requestId, ErrorCode errorCode) {
        this.requestId = requestId;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.response = null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
