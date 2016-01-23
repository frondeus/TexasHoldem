package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static void resetInstance() {
        synchronized (Lobby.class) {
            ourInstance = null;
        }
    }

    public int maxPlayerCount = 4;
    public final int startMoney = 199;
    private final ArrayList<IPlayer> players = new ArrayList<>();

    private Lobby() {
    }

    public int getPlayersCount() {
        return players.size();
    }

    @Override
    public void onClientConnected(IPlayer client) {
        if(client == null) throw new IllegalArgumentException();
        if(players.size() < maxPlayerCount) {
            Logger.status(client + ": Add player to list");
            ArrayList<String> uuids = new ArrayList<>();
            uuids.add(client.getUUID().toString());
            for(IPlayer all: players)
                uuids.add(all.getUUID().toString());

            players.add(client);

            client.setMoney(startMoney);

            ServerResponse response = new ServerResponse()
                    .setStatus(ServerResponse.Status.Ok)
                    .setMessage("Welcome");

            try {
                client.sendMessage(response);
            }
            catch (IOException e) {
                Logger.exception(e);
            }


            ServerEvent connectEvent = new ServerEvent()
                    .setType(ServerEvent.Type.Connected)
                    .setArguments(uuids.toArray(new String[]{}));
            try {
                client.sendMessage(connectEvent);
            }
            catch(IOException e) {
                Logger.exception(e);
            }

            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientConnect)
                    .setArguments(new String[] {client.getUUID().toString()});
            try {
                broadcastExcept(client,event);
            } catch (IOException e) {
                Logger.exception(e);
            }

            //TODO: ready!?
            if(players.size() >= maxPlayerCount) {
                GamePlay.getInstance().setPlayers(players);
                Server.getInstance().setState(GamePlay.getInstance());
                event = new ServerEvent()
                        .setType(ServerEvent.Type.ChangeState)
                        .setArguments(new String[] {"GamePlay"});
                try {
                    broadcast(event);
                }
                catch(IOException e) {
                    Logger.exception(e);
                }
            }
        }
        else {
            Logger.status(client + ": Full");
            Logger.status(client + ": We need to disconnect client");

            ServerResponse response = new ServerResponse()
                    .setStatus(ServerResponse.Status.Failure)
                    .setMessage("Full server");
            try {
                client.sendMessage(response);
            } catch (IOException e) {
                Logger.exception(e);
            }
            client.disconnect();
        }
    }

    private void broadcast(ServerMessage message) throws IOException {
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
    public void onClientMessage(IPlayer client, ClientMessage message) {
        if(client == null) throw new IllegalArgumentException();
        if(message == null) throw  new IllegalArgumentException();

        ServerResponse response;

        if(players.indexOf(client) < 0) {
            response = new ServerResponse()
                    .setStatus(ServerResponse.Status.Failure)
                    .setMessage("Not connected");
            try {
                client.sendMessage(response);
            } catch (IOException e) {
                Logger.exception(e);
            }
            return;
        }

        String args = String.join(" ", message.getArguments());
        Logger.status(client + ": " + message.getCommand() + " " + args);

        switch (message.getCommand()) {
            case "chat":
                response = new ServerResponse()
                        .setStatus(ServerResponse.Status.Ok)
                        .setMessage("Send");
                try {
                    client.sendMessage(response);
                } catch (IOException e) {
                    Logger.exception(e);
                }

                ArrayList<String> chatArguments = new ArrayList<>();
                chatArguments.add(client.getUUID().toString());
                chatArguments.addAll(Arrays.asList(message.getArguments()));
                ServerEvent event = new ServerEvent()
                        .setType(ServerEvent.Type.Chat)
                        .setArguments(chatArguments.toArray(new String[]{}));
                try {
                    broadcast(event);
                } catch (IOException e) {
                    Logger.exception(e);
                }
                break;
            default:
                response = new ServerResponse()
                        .setStatus(ServerResponse.Status.Failure)
                        .setMessage("Invalid command");
                try {
                    client.sendMessage(response);
                } catch (IOException e) {
                    Logger.exception(e);
                }
                break;
        }
    }

    @Override
    public void onClientDisconnected(IPlayer client) {
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            players.remove(client);
            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientDisconnect)
                    .setArguments(new String[] {client.getUUID().toString()});
            try {
                broadcastExcept(client,event);
            } catch (IOException e) {
                Logger.exception(e);
            }
        }
    }
}
