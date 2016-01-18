package lubiezurek.texasholdem.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by frondeus on 27.12.2015.
 */
public class PlayerMock implements IPlayer {
    private UUID uuid;
    private int money;
    private ArrayList<ServerMessage> messages = new ArrayList<>();
    private boolean disconnected;

    public PlayerMock() {
        uuid = UUID.randomUUID();
        money = 0;
        disconnected = false;
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
        this.money = money;
    }

    @Override
    public void sendMessage(ServerMessage message) throws IOException {
        messages.add(message);
    }

    @Override
    public void disconnect() {
        disconnected = true;
    }

    @Override
    public String toString() {
        return "PlayerMock: " + uuid.toString();
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public ServerMessage[] getLastMessages() {
        ServerMessage[] msg = messages.toArray(new ServerMessage[] {});
        messages.clear();
        return msg;
    }
}