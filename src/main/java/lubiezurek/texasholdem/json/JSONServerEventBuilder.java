package main.java.lubiezurek.texasholdem.json;

import main.java.lubiezurek.texasholdem.server.IServerMessageBuilder;
import main.java.lubiezurek.texasholdem.server.ServerEvent;
import main.java.lubiezurek.texasholdem.server.ServerMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frondeus on 07.12.2015.
 */
public class JSONServerEventBuilder implements IServerMessageBuilder {

    @Override
    public ServerMessage deserializeMessage(String input) {
        JSONObject json = new JSONObject(input);
        ServerEvent event = new ServerEvent();

        event.setType(ServerEvent.Type.valueOf(json.getString("type")));

        JSONArray array = json.getJSONArray("arguments");
        if(array != null) {
            ArrayList<String> list = new ArrayList<>();
            int length = array.length();
            for(int i = 0; i < length; i++)
                list.add(array.getString(i));

            String[] stackArray = new String[length];
            event.setArguments(list.toArray(stackArray));
        }

        return event;
    }

    @Override
    public String serializeMessage(ServerMessage message) {
        JSONObject json  = new JSONObject();
        ServerEvent event = (ServerEvent) message;

        json.put(JSONServerMessageBuilder.messageTypeKey, event.getClass().toString());
        json.put("type", event.getEventType().toString());
        json.put("arguments", event.getArguments());

        return json.toString();
    }
}
