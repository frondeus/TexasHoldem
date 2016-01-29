package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.server.model.card.Card;

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
    private IPlayer nextPlayer;
    private Card[] hand = new Card[2];
    private PlayerState state = PlayerState.WAITING;

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
    public void takeAwayMoney(int money) {
        this.money -= money;
    }

    @Override
    public void sendMessage(ServerMessage message) {
        messages.add(message);
    }

    @Override
    public void disconnect() {
        disconnected = true;
    }

    @Override
    public IPlayer getNextPlayer() {
        return nextPlayer;
    }

    @Override
    public void setNextPlayer(IPlayer player) {
        nextPlayer = player;
    }

    @Override
    public Card[] getHand() {
        return hand;
    }

    @Override
    public void setHand(Card[] cards) {
        hand = cards;
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

    @Override
    public void setPlayerState(PlayerState state){
        this.state = state;
    }

    @Override
    public PlayerState getPlayerState(){
        return this.state;
    }
}
