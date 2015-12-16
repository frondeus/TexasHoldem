package main.java.lubiezurek.texasholdem.server.states;

import main.java.lubiezurek.texasholdem.Logger;
import main.java.lubiezurek.texasholdem.client.ClientMessage;
import main.java.lubiezurek.texasholdem.server.*;

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

            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientConnect)
                    .setArguments(new String[] {});
            try {
                broadcastExcept(client,event);
            } catch (IOException e) {
                Logger.exception(e);
            }
        }
        else {
            Logger.status(client + ": Full");
            Logger.status(client + ": We need to disconnect client");
            client.disconnect();
        }
    }

    public void broadcast(ServerMessage message) throws IOException {
        for(ServerClientThread all: clients) {
            all.sendMessage(message);
        }
    }

    public void broadcastExcept(ServerClientThread client, ServerMessage message) throws IOException {
        for(ServerClientThread all: clients) {
            if(all != client)    all.sendMessage(message);
        }
    }

    @Override
    public void onClientMessage(ServerClientThread client, ClientMessage message) {
        String args = String.join(" ", message.getArguments());
        Logger.status(client + ": " + message.getCommand() + " " + args);

        if(message.getCommand().equals("chat")) {
            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.Chat)
                    .setArguments(message.getArguments());
            try {
                broadcast(event);
            }
            catch(IOException e) {
                Logger.exception(e);
            }
        }
        else {
            ServerResponse response = new ServerResponse()
                    .setStatus(ServerResponse.Status.Failure)
                    .setMessage("Invalid command");
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
            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientDisconnect)
                    .setArguments(new String[] {});
            try {
                broadcastExcept(client,event);
            } catch (IOException e) {
                Logger.exception(e);
            }
        }
    }
}
