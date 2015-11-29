package lubiezurek.texasholdem.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import lubiezurek.texasholdem.Logger;

public class Client {
	Socket socket;
	
	public Client(String[] args) throws UnknownHostException, IOException {
		String address = args[1];
		int port = Integer.parseInt(args[2]);
		Logger.status("Connecting to: " + address + ":" + port);
		socket = new Socket(address, port);
	}
	
	public void run() {
		try { 
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			String response = in.readUTF();
			
			if(response.equals("Connected")) {
				Logger.status("Connected");
				String command;
				do {
					command = System.console().readLine();
					out.writeUTF(command);
					response = in.readUTF();
					Logger.status("> " + response);
				} while(!response.equals("Disconnected"));
			}
			else {
				Logger.error(response);
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
