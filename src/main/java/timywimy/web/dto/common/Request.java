package timywimy.web.dto.common;

import java.util.UUID;

public class Request<T> {
    private Integer requestId;
    private UUID session;
    private T request;

    public Request(T request, Integer requestId) {
        this.requestId = requestId;
        this.request = request;
    }

    public Request(T request, UUID session, Integer requestId) {
        this.requestId = requestId;
        this.request = request;
        this.session = session;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }
}
