package lubiezurek.texasholdem.client;

/**
 * Created by frondeus on 06.12.2015.
 * Creates messages
 */
public interface IClientMessageBuilder {
    ClientMessage deserializeMessage(String input);
    String serializeMessage(ClientMessage message);
}
