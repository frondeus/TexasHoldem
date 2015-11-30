package lubiezurek.texasholdem.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import lubiezurek.texasholdem.Logger;

public class Server {
	private ServerSocket serverSocket;
	private ArrayList<ServerClientThread> clients;
	
	public Server(String[] args) throws IOException {
		int port = Integer.parseInt(args[1]);

		Logger.status("Create server on port: " + args[1]);
		clients = new ArrayList<ServerClientThread>();
		serverSocket = new ServerSocket(port);
	}
	
	public void run() {
		while(true) {
			try {
				Socket clientSocket = serverSocket.accept();
				ServerClientThread thread = new ServerClientThread(clientSocket);
				clients.add(thread);
				thread.run();
			}
			catch(SocketTimeoutException e) {
				Logger.error("Socket timed out!");
				return;
			}
			catch(IOException e) {
				Logger.exception(e);
				return;
			}
		}
	}
}
