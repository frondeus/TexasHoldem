package lubiezurek.texasholdem.client;

/**
 * Created by frondeus on 06.12.2015.
 * Message send by client to server
 */
public interface IClientMessage {
    String getCommand();
    String[] getArguments();

    IClientMessage setCommand(String command);
    IClientMessage setArguments(String[] arguments);

    IClientMessage fromString(String input);
}
