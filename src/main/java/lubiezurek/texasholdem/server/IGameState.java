package main.java.lubiezurek.texasholdem.server;

import main.java.lubiezurek.texasholdem.client.ClientMessage;

/**
 * Created by frondeus on 05.12.2015.
 * Game state like lobby, gameplay, after game.
 */
public interface IGameState {
    void onClientConnected(ServerClientThread client);
    void onClientMessage(ServerClientThread client, ClientMessage message);
    void onClientDisconnected(ServerClientThread client);
}
