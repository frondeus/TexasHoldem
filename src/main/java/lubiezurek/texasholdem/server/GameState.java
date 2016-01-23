package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.client.ClientMessage;

import java.util.ArrayList;

/**
 * Created by frondeus on 05.12.2015.
 * Game state like lobby, gameplay, after game.
 */
public abstract class GameState {
    public abstract void onEnter();
    public abstract void onClientConnected(IPlayer client);
    public abstract void onClientMessage(IPlayer client, ClientMessage message);
    public abstract void onClientDisconnected(IPlayer client);

    protected ArrayList<IPlayer> players = new ArrayList<>();

    public void broadcast(ServerMessage message) {
        for(IPlayer all: players) {
            all.sendMessage(message);
        }
    }

    public void broadcastExcept(IPlayer client, ServerMessage message) {
        for(IPlayer all: players) {
            if(all != client)    all.sendMessage(message);
        }
    }

    public int getPlayersCount() {
        return players.size();
    }
    public void setPlayers(ArrayList<IPlayer> players) {
        this.players = players;
    }

}
