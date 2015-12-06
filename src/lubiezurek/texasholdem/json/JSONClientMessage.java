package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.client.IClientMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frondeus on 06.12.2015.
 * Default message in JSON format
 */
public class JSONClientMessage implements IClientMessage {
    private String command;
    private String[] arguments;

    public JSONClientMessage() {
        command = "";
        arguments = new String[] {};
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public IClientMessage setCommand(String command) {
        this.command = command;
        return this;
    }

    @Override
    public IClientMessage setArguments(String[] arguments) {
        this.arguments = arguments;
        return this;
    }

    @Override
    public IClientMessage fromString(String input) {
        JSONObject json = new JSONObject(input);
        this.command = json.getString("command");

        ArrayList<String> args = new ArrayList<>();
        JSONArray arguments = json.getJSONArray("arguments");
        if(arguments != null) {
            int length = arguments.length();
            for(int i = 0; i < length; i++) args.add(arguments.getString(i));
        }
        String[] stockArray = new String[args.size()];
        this.arguments = args.toArray(stockArray);

        return this;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("command", this.command);
        json.put("arguments", this.arguments);
        return json.toString();
    }
}
