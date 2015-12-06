package lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 06.12.2015.
 */
public abstract class ServerResponse extends ServerMessage {
    public enum Status {
        Ok,
        Failure
    }

    @Override
    public Type getType() {
        return Type.Response;
    }

    public abstract Status getStatus();
    public abstract String getMessage();

    public abstract ServerResponse setStatus(Status status);
    public abstract ServerResponse setMessage(String message);
}
