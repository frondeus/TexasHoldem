package lubiezurek.texasholdem.server.gamestates;

import com.sun.net.httpserver.Authenticator;
import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by frondeus on 06.12.2015.
 * Lobby. Players waits until game starts. Players can set game settings.
 */
public class Lobby extends GameState {
    private volatile static Lobby instance;
    public static Lobby getInstance() {
        if(instance == null) {
            synchronized (Lobby.class) {
                if(instance == null) {
                    instance = new Lobby();
                }
            }
        }
        return instance;
    }
    public static void resetInstance() {
        synchronized (Lobby.class) {
            instance = null;
        }
    }


    private Lobby() {
    }

    public void onEnter() {

    }


    public void onClientConnected(IPlayer client) {
        if(client == null) throw new IllegalArgumentException();
        if(players.size() < Server.getInstance().Options.getMaxPlayerCount()) {
            Logger.status(client + ": Add player to list");

            ArrayList<String> uuids = new ArrayList<>();
            uuids.add(client.getUUID().toString());
            for(IPlayer all: players)
                uuids.add(all.getUUID().toString());

            players.add(client);

            client.sendMessage(new ServerEvent(ServerEvent.Type.Connected, uuids));

            broadcastExcept(client, new ServerEvent(ServerEvent.Type.ClientConnect,
                    new String[]{client.getUUID().toString()}));
        }
        else {
            Logger.status(client + ": Full");
            Logger.status(client + ": We need to disconnect client");

            client.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                    "Full server"));
            client.disconnect();
        }
    }

    @Override
    public void changeState() {
        if(players.size() >= Server.getInstance().Options.getMaxPlayerCount()) {
            GamePlay.getInstance().setPlayers(players);
            Server.getInstance().setState(GamePlay.getInstance());
        }
    }

    public void onClientMessage(IPlayer client, ClientMessage message) {
        if(client == null) throw new IllegalArgumentException();
        if(message == null) throw  new IllegalArgumentException();

        if(players.indexOf(client) < 0) {
            client.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                    "Not connected"));
            return;
        }

        switch (message.getCommand()) {
            case "chat":
                ArrayList<String> chatArguments = new ArrayList<>();
                chatArguments.add(client.getUUID().toString());
                chatArguments.addAll(Arrays.asList(message.getArguments()));
                broadcast(new ServerEvent(ServerEvent.Type.Chat, chatArguments));
                break;
            default:
                client.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Invalid command"));
                break;
        }
    }

    public void onClientDisconnected(IPlayer client) {
        if(client == null) throw new IllegalArgumentException();
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            players.remove(client);

            broadcastExcept(client, new ServerEvent(ServerEvent.Type.ClientDisconnect,
                    new String[]{ client.getUUID().toString() }));
        }
    }

}
