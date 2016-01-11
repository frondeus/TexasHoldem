package lubiezurek.texasholdem.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.IClientMessageBuilder;
import lubiezurek.texasholdem.json.JSONClientMessageBuilder;
import lubiezurek.texasholdem.json.JSONServerMessageBuilder;
import lubiezurek.texasholdem.server.states.Lobby;

public class Server extends WebSocketServer {
    
    private volatile static Server instance;
    public static Server getInstance() {
        return getInstance(7777);
    }
    public static Server getInstance(int port) {
        if(instance == null) {
            synchronized(Server.class) {
                if(instance == null) instance = new Server(port);
            }
        }
        return instance;
    }

    private IGameState state;
    private final IClientMessageBuilder clientMessageBuilder;
    private final IServerMessageBuilder serverMessageBuilder;

    private Server(int port) {
        super(new InetSocketAddress(port));
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


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress()
                + " with " + code + " " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("on message: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("on error: " + ex);
    }

    public IGameState getState() {
        return this.state;
    }

    public void setState(IGameState state) {
        this.state = state;
    }

}

/*
public class Server {
    public void run(String[] args) throws IOException {

        int port = Integer.parseInt(args[1]);
        Logger.status("Create server on port: " + args[1]);
        ServerSocket serverSocket = new ServerSocket(port);

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
}
*/
