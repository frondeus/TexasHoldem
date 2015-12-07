package lubiezurek.texasholdem.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.IClientMessageBuilder;
import lubiezurek.texasholdem.json.JSONClientMessageBuilder;
import lubiezurek.texasholdem.json.JSONServerMessageBuilder;
import lubiezurek.texasholdem.server.states.Lobby;

public class Server {
    private volatile static Server instance;
    public static Server getInstance() {
        if(instance == null) {
            synchronized (Server.class) {
                if(instance == null) instance = new Server();
            }
        }
        return instance;
    }

	private ServerSocket serverSocket;
    private IGameState state;
    private IClientMessageBuilder clientMessageBuilder;
    private IServerMessageBuilder serverMessageBuilder;

    private Server() {
        this.clientMessageBuilder = new JSONClientMessageBuilder();
        this.serverMessageBuilder = new JSONServerMessageBuilder();
        this.state = Lobby.getInstance();
	}

    public IClientMessageBuilder getClientMessageBuilder() {
        return clientMessageBuilder;
    }

    public IServerMessageBuilder getServerMessageBuilder() {
        return serverMessageBuilder;
    }

	public void run(String[] args) throws IOException {

        int port = Integer.parseInt(args[1]);
        Logger.status("Create server on port: " + args[1]);
        this.serverSocket = new ServerSocket(port);

        while(true) {
			try {
				Socket clientSocket = serverSocket.accept();
				ServerClientThread thread = new ServerClientThread(this, clientSocket);
                thread.start();
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

    public IGameState getState() {
        return state;
    }
}
