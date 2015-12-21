package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.client.IClientMessageBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frondeus on 06.12.2015.
 * Creates JSON Messages
 */
public class JSONClientMessageBuilder implements IClientMessageBuilder {
    @Override
    public ClientMessage deserializeMessage(String input) {
        JSONObject json = new JSONObject(input);
        ClientMessage message = new ClientMessage();

        message.setCommand(json.getString("command"));

        ArrayList<String> list = new ArrayList<>();
        JSONArray arguments = json.getJSONArray("arguments");
        if(arguments != null) {
            int length = arguments.length();
            for(int i = 0; i < length; i++)
                list.add(arguments.getString(i));

            String[] stockArray = new String[list.size()];
            message.setArguments(list.toArray(stockArray));
        }

        return message;
    }

    @Override
    public String serializeMessage(ClientMessage message) {
        JSONObject json = new JSONObject();
        json.put("command", message.getCommand());
        json.put("arguments", message.getArguments());
        return json.toString();
    }
}
