package lubiezurek.texasholdem.client;

/**
 * Created by frondeus on 06.12.2015.
 * Message send by client to server
 */
public class ClientMessage {
    private String command = "";
    private String[] arguments = new String[]{};


    public String getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public ClientMessage setCommand(String command) {
        this.command = command;
        return this;
    }

    public ClientMessage setArguments(String[] arguments) {
        this.arguments = arguments;
        return this;
    }
}
