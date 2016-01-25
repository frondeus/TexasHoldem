package lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 07.12.2015.
 */
public class ServerEvent extends ServerMessage {
    public enum Type {
        Connected,
        ClientConnect,
        ClientDisconnect,
        Commands,
        Chat,
        ChangeState,
        Hand, SharedCard, Turn
    }

    private Type type;
    private String[] arguments;

    public Type getEventType() {
        return type;
    }

    public String[] getArguments() {
        return arguments;
    }

    public ServerEvent setType(Type type) {
        if(type == null) throw new IllegalArgumentException();
        this.type = type;
        return this;
    }

    public ServerEvent setArguments(String[] arguments) {
        if(arguments == null) throw new IllegalArgumentException();

        this.arguments = arguments;
        return this;
    }
}

