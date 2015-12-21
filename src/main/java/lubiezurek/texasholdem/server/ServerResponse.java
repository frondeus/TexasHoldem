package lubiezurek.texasholdem.server;

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
        if(status == null) throw new IllegalArgumentException();
        this.status = status;
        return this;
    }

    public ServerResponse setMessage(String message) {
        if(message == null) throw new IllegalArgumentException();
        this.message = message;
        return this;
    }
}
