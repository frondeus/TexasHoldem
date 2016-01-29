package lubiezurek.texasholdem.client;

/**
 * Created by frondeus on 06.12.2015.
 * Message send by client to server
 */
public class ClientMessage {
    private String command = "";
    private String[] arguments = new String[]{};

    public ClientMessage(String command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
