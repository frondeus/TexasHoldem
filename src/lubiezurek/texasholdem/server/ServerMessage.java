package lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 06.12.2015.
 */
public abstract class ServerMessage {
    public enum Type {
        Response,
        Event
    }

    public abstract Type getType();
    public abstract ServerMessage fromString(String input);
}
