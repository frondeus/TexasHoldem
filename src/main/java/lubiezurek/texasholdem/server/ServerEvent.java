package lubiezurek.texasholdem.server;

import java.util.ArrayList;

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
        Hand, SharedCard, OtherHand, Bet, Turn
    }

    private Type type;
    private String[] arguments;

    public ServerEvent(Type type, String[] arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public ServerEvent(Type type, ArrayList<String> arguments) {
        this(type, arguments.toArray(new String[]{}));
    }

    public Type getEventType() {
        return type;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}

