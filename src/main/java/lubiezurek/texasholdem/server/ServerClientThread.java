package lubiezurek.texasholdem.server;

import java.io.EOFException;
import java.io.IOException;
import org.java_websocket.WebSocket;
import java.util.UUID;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;

public class ServerClientThread extends Thread implements IPlayer {
    private final Server server;
    private final WebSocket socket;

    private int money = 100;
    private UUID uuid;

    public ServerClientThread(Server server, WebSocket socket) {
        this.server = server;
        this.socket = socket;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return socket.getRemoteSocketAddress().toString();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }


    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public void setMoney(int money) {
        if(money < 0) throw new IllegalArgumentException();
        this.money = money;
    }

    public void sendMessage(ServerMessage message) throws IOException {
        socket.send(this.server.getServerMessageBuilder().serializeMessage(message));
    }

    public void disconnect() {
        socket.close();
    }
}
