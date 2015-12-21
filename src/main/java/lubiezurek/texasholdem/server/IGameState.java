package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.client.ClientMessage;

/**
 * Created by frondeus on 05.12.2015.
 * Game state like lobby, gameplay, after game.
 */
public interface IGameState {
    void onClientConnected(ServerClientThread client);
    void onClientMessage(IPlayer client, ClientMessage message);
    void onClientDisconnected(ServerClientThread client);
}
