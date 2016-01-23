package lubiezurek.texasholdem.server;

import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

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
    private HashMap<WebSocket, Client> clients;

    private Server(int port) {
        super(new InetSocketAddress(port));
        this.clientMessageBuilder = new JSONClientMessageBuilder();
        this.serverMessageBuilder = new JSONServerMessageBuilder();
        this.state = Lobby.getInstance();
        this.clients = new HashMap<>();

    }

    public IClientMessageBuilder getClientMessageBuilder() {
        return clientMessageBuilder;
    }

    public IServerMessageBuilder getServerMessageBuilder() {
        return serverMessageBuilder;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection " + conn.getRemoteSocketAddress());
        Client client = new Client(this, conn);

        clients.put(conn, client);
        state.onClientConnected(client);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Client client = clients.get(conn);
        state.onClientDisconnected(client);

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Client client = clients.get(conn);
        state.onClientMessage(client, clientMessageBuilder.deserializeMessage(message));
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
