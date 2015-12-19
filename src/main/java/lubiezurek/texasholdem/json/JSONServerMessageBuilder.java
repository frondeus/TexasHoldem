package main.java.lubiezurek.texasholdem.json;

import main.java.lubiezurek.texasholdem.server.IServerMessageBuilder;
import main.java.lubiezurek.texasholdem.server.ServerEvent;
import main.java.lubiezurek.texasholdem.server.ServerMessage;
import main.java.lubiezurek.texasholdem.server.ServerResponse;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by frondeus on 06.12.2015.
 */
public class JSONServerMessageBuilder implements IServerMessageBuilder {
    private final HashMap<Class, IServerMessageBuilder> classBuilders = new HashMap<>();
    private final HashMap<String, IServerMessageBuilder> stringBuilders = new HashMap<>();
    public final static String messageTypeKey = "messageType";

    public JSONServerMessageBuilder() {
        addBuilder(ServerResponse.class, new JSONServerResponseBuilder());
        addBuilder(ServerEvent.class, new JSONServerEventBuilder());
    }

    private void addBuilder(Class type, IServerMessageBuilder builder) {
        classBuilders.put(type, builder);
        stringBuilders.put(type.toString(), builder);
    }

    @Override
    public ServerMessage deserializeMessage(String input) {
        JSONObject json = new JSONObject(input);
        String type = json.getString(messageTypeKey);

        if(stringBuilders.containsKey(type)) {
            return stringBuilders.get(type).deserializeMessage(input);
        }
        else {
            throw new RuntimeException("There is no class: " + type);
        }
    }

    @Override
    public String serializeMessage(ServerMessage message) {
        Class type = message.getClass();

        if(classBuilders.containsKey(type)) {
            return classBuilders.get(type).serializeMessage(message);
        }
        else {
            throw new RuntimeException("There is no class: " + type);
        }
    }
}
