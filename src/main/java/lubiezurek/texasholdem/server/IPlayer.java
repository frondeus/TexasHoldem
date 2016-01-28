package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.server.model.card.Card;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by frondeus on 14.12.2015.
 */
public interface IPlayer {
    UUID getUUID();
    int getMoney();

    void setMoney(int money);

    void sendMessage(ServerMessage message);

    void disconnect();

    IPlayer getNextPlayer();
    void setNextPlayer(IPlayer player);

    Card[] getHand();
    void setHand(Card[] cards);

    void setPlayerState(PlayerState state);
    PlayerState getPlayerState();
}
