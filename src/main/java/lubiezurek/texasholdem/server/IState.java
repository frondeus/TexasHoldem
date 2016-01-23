package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.client.ClientMessage;

/**
 * Created by frondeus on 23.01.16.
 */
public interface IState {
    void onPlayerMessage(IPlayer player, ClientMessage message);
    void onStart();
    String[] getAvailableCommands();
}
