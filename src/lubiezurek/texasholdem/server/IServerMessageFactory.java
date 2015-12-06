package lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 06.12.2015.
 */
public interface IServerMessageFactory {
    ServerResponse createResponse();
    ServerMessage createMessage(String input);
}
