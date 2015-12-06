package lubiezurek.texasholdem.json;

import lubiezurek.texasholdem.server.ServerMessage;
import lubiezurek.texasholdem.server.ServerResponse;
import org.json.JSONObject;

/**
 * Created by frondeus on 06.12.2015.
 */
public class JSONServerResponse extends ServerResponse {
    private Status status;
    private String message;

    public JSONServerResponse() {
        status = Status.Ok;
        message = "";
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ServerResponse setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public ServerResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ServerMessage fromString(String input) {
        JSONObject json = new JSONObject(input);
        this.message = json.getString("message");
        switch(json.getString("status")){
            case "ok":
                this.status = Status.Ok;
                break;
            case "failure":
                this.status = Status.Failure;
                break;
        }

        return this;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("status", status==Status.Ok?"ok":"failure");
        json.put("message", message);
        json.put("type", "response");
        return json.toString();
    }
}
