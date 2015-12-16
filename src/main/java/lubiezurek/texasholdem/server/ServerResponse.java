package main.java.lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 06.12.2015.
 */
public class ServerResponse extends ServerMessage {
    public enum Status {
        Ok,
        Failure
    }

    private Status status;
    private String message;

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ServerResponse setStatus(Status status) {
        this.status = status;
        return this;
    }

    public ServerResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
