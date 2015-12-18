package main.java.lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 07.12.2015.
 */
public class ServerEvent extends ServerMessage {
    public enum Type {
        ClientConnect,
        ClientDisconnect,
        Chat
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
        this.type = type;
        return this;
    }

    public ServerEvent setArguments(String[] arguments) {
        this.arguments = arguments;
        return this;
    }
}
