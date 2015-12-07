package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.server.IServerMessageBuilder;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.ServerMessage;
import lubiezurek.texasholdem.server.ServerResponse;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by frondeus on 06.12.2015.
 */
public class JSONServerMessageBuilder implements IServerMessageBuilder {
    private HashMap<Class, IServerMessageBuilder> classBuilders = new HashMap<>();
    private HashMap<String, IServerMessageBuilder> stringBuilders = new HashMap<>();
    public static String messageTypeKey = "messageType";

    public JSONServerMessageBuilder() {
        addBuilder(ServerResponse.class, new JSONServerResponseBuilder());
        addBuilder(ServerEvent.class, new JSONServerEventBuilder());
    }

    public void addBuilder(Class type, IServerMessageBuilder builder) {
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
