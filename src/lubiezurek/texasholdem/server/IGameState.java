package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.client.IClientMessage;

/**
 * Created by frondeus on 05.12.2015.
 * Game state like lobby, gameplay, after game.
 */
public interface IGameState {
    void onClientConnected(ServerClientThread client);
    void onClientMessage(ServerClientThread client, IClientMessage message);
    void onClientDisconnected(ServerClientThread client);
}
