package lubiezurek.texasholdem.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.server.IResponse;
import lubiezurek.texasholdem.server.IResponseFactory;
import lubiezurek.texasholdem.server.json.JSONResponseFactory;

public class Client {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	private IResponseFactory responseFactory;
	
	public Client(String[] args) throws UnknownHostException, IOException {
		responseFactory = new JSONResponseFactory();
		
		String address = args[1];
		int port = Integer.parseInt(args[2]);
		Logger.status("Connecting to: " + address + ":" + port);
		socket = new Socket(address, port);
	}
	
	public IResponse getResponse() throws IOException {
		return responseFactory.CreateResponse().fromString(in.readUTF());
	}
	
	public void run() {
		try { 
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			IResponse response = getResponse();
			
			if(response.getStatus().equals("Connected")) { 
				Logger.status("Connected");
				String command;
				do {
					
					command = System.console().readLine();
					out.writeUTF(command);
					
					
					response = getResponse();
					
					Logger.status("> " + response);
				} while (! response.getStatus().equals("Disconnected"));
			}
			else {
				Logger.error(response.getStatus());
				Logger.error(response.getMessage());
			}
			
			Logger.status("Exitting");
			
			socket.close();
		}
		catch (IOException e) {
			Logger.exception(e);
			return;
		}
	}
}
