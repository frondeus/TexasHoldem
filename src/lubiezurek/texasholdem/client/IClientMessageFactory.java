package lubiezurek.texasholdem.client;

/**
 * Created by frondeus on 06.12.2015.
 * Creates messages
 */
public interface IClientMessageFactory {
    IClientMessage createMessage();
    IClientMessage createMessage(String input);
}
