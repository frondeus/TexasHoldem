package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.server.IServerMessageFactory;
import lubiezurek.texasholdem.server.ServerMessage;
import lubiezurek.texasholdem.server.ServerResponse;
import org.json.JSONObject;

/**
 * Created by frondeus on 06.12.2015.
 */
public class JSONServerMessageFactory implements IServerMessageFactory {
    @Override
    public ServerResponse createResponse() {
        return new JSONServerResponse();
    }

    @Override
    public ServerMessage createMessage(String input) {
        JSONObject json = new JSONObject(input);
        if(json.getString("type").equals("response"))
            return new JSONServerResponse().fromString(input);

        //TODO:
        return null;
    }
}
