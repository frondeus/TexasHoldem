package lubiezurek.texasholdem.server;

public interface IResponse {
	
	public IResponse setStatus(String status);
	public String getStatus();
	
	public IResponse setMessage(String message);
	public String getMessage();
	
	public IResponse setAvailableCommands(String[] availableCommands);
	public String[] getAvailableCommands();
	
	public String toString();
	public IResponse fromString(String string);
}
