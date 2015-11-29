package lubiezurek.texasholdem.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import lubiezurek.texasholdem.Logger;

public class ServerClientThread extends Thread {
	Socket socket;
	DataInputStream in; 
	DataOutputStream out;
	
	public ServerClientThread(Socket socket) {
		this.socket = socket;
		Logger.status("New connection: " + socket.getRemoteSocketAddress().toString());
	}
	
	private void establishConnection() throws IOException {
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		
		out.writeUTF("Connected");
	}
	
	@Override
	public void run() {

		try {
			String command;
			establishConnection();
			while(true) {
				command = in.readUTF();
				Logger.status("> " + command);
				
				if(command.equals("exit")) {
					out.writeUTF("Disconnected");
					break;
				}
				else {
					out.writeUTF("Response");
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
