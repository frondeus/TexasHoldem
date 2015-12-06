package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.IClientMessage;
import lubiezurek.texasholdem.server.IGameState;
import lubiezurek.texasholdem.server.Server;
import lubiezurek.texasholdem.server.ServerClientThread;
import lubiezurek.texasholdem.server.ServerResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by frondeus on 06.12.2015.
 * Lobby. Players waits until game starts. Players can set game settings.
 */
public class Lobby implements IGameState {
    private volatile static Lobby ourInstance;
    public static Lobby getInstance() {
        if(ourInstance == null) {
            synchronized (Lobby.class) {
                if(ourInstance == null) {
                    ourInstance = new Lobby();
                }
            }
        }
        return ourInstance;
    }

    private final int maxPlayerCount = 2;
    private final ArrayList<ServerClientThread> clients = new ArrayList<>();

    private Lobby() {
    }

    @Override
    public void onClientConnected(ServerClientThread client) {
        if(clients.size() < maxPlayerCount) {
            Logger.status(client + ": Add player to list");
            clients.add(client);
        }
        else {
            Logger.status(client + ": Full");
            Logger.status(client + ": We need to disconnect client");
            client.disconnect();
        }
    }

    @Override
    public void onClientMessage(ServerClientThread client, IClientMessage message) {
        Logger.status(client + ": " + message.getCommand());

        if(message.getCommand().equals("chat")) {
        }
        else {
            ServerResponse response = Server.getInstance().getServerMessageFactory().createResponse();
            response.setStatus(ServerResponse.Status.Failure).setMessage("Invalid command");
            try {
                client.sendMessage(response);
            } catch (IOException e) {
                Logger.exception(e);
            }
        }
    }

    @Override
    public void onClientDisconnected(ServerClientThread client) {
        Logger.status(client + ": Disconnected");
        if(clients.indexOf(client) >= 0) {
            clients.remove(client);
        }
    }
}
