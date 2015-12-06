package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.client.IClientMessage;
import lubiezurek.texasholdem.client.IClientMessageFactory;

/**
 * Created by frondeus on 06.12.2015.
 * Creates JSON Messages
 */
public class JSONClientMessageFactory implements IClientMessageFactory {
    @Override
    public IClientMessage createMessage() {
        return new JSONClientMessage();
    }

    @Override
    public IClientMessage createMessage(String input) {
        return new JSONClientMessage().fromString(input);
    }
}
