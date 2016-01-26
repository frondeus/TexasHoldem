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

    public ServerResponse(Status status, String message) {
        if(status == null) throw new IllegalArgumentException();
        if(message == null) throw new IllegalArgumentException();
        this.status = status;
        this.message = message;
    }

    public ServerResponse(String message) {
        this(Status.Failure, message);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
