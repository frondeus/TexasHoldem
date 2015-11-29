package lubiezurek.texasholdem.server.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import lubiezurek.texasholdem.server.IResponse;

public class JSONResponse implements IResponse {
	
	private String status;
	private String message;
	private String[] availableCommands;

	@Override
	public IResponse setStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public IResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public IResponse setAvailableCommands(String[] availableCommands) {
		this.availableCommands = availableCommands;
		return this;
	}

	@Override
	public String[] getAvailableCommands() {
		return availableCommands;
	}

	@Override
	public IResponse fromString(String string) {
		JSONObject json = new JSONObject(string);
		status = json.getString("status");
		message = json.getString("message");
		JSONArray availableCommands = json.getJSONArray("availableCommands");
		ArrayList<String> list = new ArrayList<String>();     
		if (availableCommands != null) { 
		   int len = availableCommands.length();
		   for (int i=0;i<len;i++){ 
		    list.add(availableCommands.get(i).toString());
		   } 
		} 
		String[] stockArr = new String[list.size()];
		this.availableCommands = list.toArray(stockArr);
		return this;
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("message", message);
		json.put("availableCommands", availableCommands);
		return json.toString();
	}

}
