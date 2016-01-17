package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by frondeus on 14.12.2015.
 */
public class GamePlay implements IGameState {
    private volatile static GamePlay ourInstance;
    public static GamePlay getInstance() {
        if(ourInstance == null) {
            synchronized (GamePlay.class) {
                if(ourInstance == null) ourInstance = new GamePlay();
            }
        }
        return ourInstance;
    }

    private ArrayList<IPlayer> players = new ArrayList<>();

    private GamePlay() {
    }

    public int getPlayersCount() {
        return players.size();
    }
    public void setPlayers(ArrayList<IPlayer> players) {
        this.players = players;
    }

    protected void broadcast(ServerMessage message) throws IOException {
        for(IPlayer all: players) {
            all.sendMessage(message);
        }
    }

    private void broadcastExcept(IPlayer client, ServerMessage message) throws IOException {
        for(IPlayer all: players) {
            if(all != client)    all.sendMessage(message);
        }
    }
    @Override
    public void onClientConnected(IPlayer client) {
        ServerResponse response = new ServerResponse()
                .setStatus(ServerResponse.Status.Failure)
                .setMessage("Game is in progress");
        try {
            client.sendMessage(response);
        } catch (IOException e) {
            Logger.exception(e);
        }
        client.disconnect();
    }

    @Override
    public void onClientMessage(IPlayer client, ClientMessage message) {

    }

    @Override
    public void onClientDisconnected(IPlayer client) {
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            //TODO: Zastapic botem

            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientDisconnect)
                    .setArguments(new String[] {client.getUUID().toString()});

            try {
                broadcastExcept(client, event);
            }
            catch(IOException e) {
                Logger.exception(e);
            }
        }
    }
}
