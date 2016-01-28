package lubiezurek.texasholdem;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.deal.Deal;

/**
 * Created by frondeus on 26.01.16.
 */
public class StateMock implements IState {
    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {

    }

    @Override
    public void onStart(Deal deal) {

    }

    @Override
    public String[] getAvailableCommands(IPlayer player) {
        return new String[] {};
    }

    @Override
    public boolean isPlayerTurn(IPlayer player) {
        return false;
    }
}
