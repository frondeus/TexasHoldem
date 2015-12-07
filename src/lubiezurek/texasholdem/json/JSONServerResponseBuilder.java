package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.server.IServerMessageBuilder;
import lubiezurek.texasholdem.server.ServerMessage;
import lubiezurek.texasholdem.server.ServerResponse;
import org.json.JSONObject;

/**
 * Created by frondeus on 07.12.2015.
 */
public class JSONServerResponseBuilder implements IServerMessageBuilder {

    @Override
    public ServerMessage deserializeMessage(String input) {
        JSONObject json = new JSONObject(input);
        ServerResponse response = new ServerResponse();

        response.setStatus(ServerResponse.Status.valueOf(json.getString("status")));
        response.setMessage(json.getString("message"));

        return response;
    }

    @Override
    public String serializeMessage(ServerMessage message) {
        JSONObject json  = new JSONObject();
        ServerResponse response = (ServerResponse) message;

        json.put(JSONServerMessageBuilder.messageTypeKey, response.getClass().toString());
        json.put("status", response.getStatus().toString());
        json.put("message", response.getMessage());

        return json.toString();
    }
}
