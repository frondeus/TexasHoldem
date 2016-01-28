package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.deal.Deal;

/**
 * Created by frondeus on 23.01.16.
 */
public interface IState {
    void onPlayerMessage(IPlayer player, ClientMessage message);
    void onStart(Deal deal);
    String[] getAvailableCommands();

    boolean isPlayerTurn(IPlayer player);
}
