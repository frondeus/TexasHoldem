package lubiezurek.texasholdem.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.server.json.JSONResponseFactory;

public class ServerClientThread extends Thread {
	private Socket socket;
	private DataInputStream in; 
	private DataOutputStream out;
	
	private IResponseFactory responseFactory;
	
	public ServerClientThread(Socket socket) {
		responseFactory = new JSONResponseFactory();
		
		this.socket = socket;
		Logger.status("New connection: " + socket.getRemoteSocketAddress().toString());
	}
	
	private void establishConnection() throws IOException {
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		
		IResponse response = responseFactory.CreateResponse();
		response.setStatus("Connected")
				.setMessage("Hello!")
				.setAvailableCommands(new String[] {
					"Disconnect"
				});

		sendResponse(response);
	}
	
	public void sendResponse(IResponse response) throws IOException {
		out.writeUTF(response.toString());
	}
	
	@Override
	public void run() {

		try {
			String command;
			establishConnection();
			while(true) {
				command = in.readUTF();
				Logger.status("> " + command);
				
				IResponse response = responseFactory.CreateResponse();
				if(command.equals("Disconnect")) {
					response.setStatus("Disconnected")
						.setMessage("Good Bye!");
					sendResponse(response);
					break;
				}
				else {
					response.setStatus("Ok")
						.setMessage("Roger that!")
						.setAvailableCommands(new String[] {
							"Disconnect"	
						});
					sendResponse(response);
				}
			}
			
			Logger.status("Disconnected");
			socket.close();
			
		} catch (IOException e) {
			Logger.exception(e);
			return;
		}
	}
}
