package lubiezurek.texasholdem.server;

import java.io.EOFException;
import java.io.IOException;
import org.java_websocket.WebSocket;
import java.util.UUID;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;

public class Client extends Thread implements IPlayer {
    private final Server server;
    private final WebSocket socket;

    private int money = 100;
    private UUID uuid;
    private IPlayer nextPlayer = null;

    public Client(Server server, WebSocket socket) {
        if(server == null) throw new IllegalArgumentException();
        if(socket == null) throw new IllegalArgumentException();
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

    public void sendMessage(ServerMessage message) {
            socket.send(this.server.getServerMessageBuilder().serializeMessage(message));
            //Logger.exception(e);
    }

    public void disconnect() {
        socket.close();
    }

    @Override
    public IPlayer getNextPlayer() {
        return nextPlayer;
    }

    @Override
    public void setNextPlayer(IPlayer player) {
        nextPlayer = player;
    }
}
