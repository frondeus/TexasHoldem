package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.states.Licitation;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by frondeus on 14.12.2015.
 */
public class GamePlay extends GameState {
    private volatile static GamePlay ourInstance;
    public static GamePlay getInstance() {
        if(ourInstance == null) {
            synchronized (GamePlay.class) {
                if(ourInstance == null) ourInstance = new GamePlay();
            }
        }
        return ourInstance;
    }

    private Deck deck;
    public Deal deal; // TODO: Zamienic na jakas liste lub stack.

    private GamePlay() {
        deck = new Deck();
        deal = new Deal();
    }

    public void onEnter() {
        if(players.size() > 0 ) {
            Collections.shuffle(players);

            IPlayer player = null, lastPlayer = null;
            for(int i = 0; i < players.size(); i++) {
                if(player != null) lastPlayer = player;
                player = players.get(i);
                player.setNextPlayer(players.get(0));
                if(lastPlayer != null)
                    lastPlayer.setNextPlayer(player);
            }

            deal.setState(Licitation.getInstance());
            deal.setPlayer(players.get(0));
        }
    }

    public void onClientConnected(IPlayer client) {
        ServerResponse response = new ServerResponse()
                .setStatus(ServerResponse.Status.Failure)
                .setMessage("Game is in progress");
        client.sendMessage(response);
        client.disconnect();
    }

    public void onClientMessage(IPlayer client, ClientMessage message) {
        IPlayer currentPlayer = deal.getCurrentPlayer();
        if(currentPlayer == client)
            deal.getState().onPlayerMessage(client, message);
        else {
            ServerResponse response = new ServerResponse()
                    .setStatus(ServerResponse.Status.Failure)
                    .setMessage("It's not your turn");
            client.sendMessage(response);
        }
    }

    public void sendTurnEvent() {
        IPlayer currentPlayer = deal.getCurrentPlayer();

        ServerEvent event = new ServerEvent()
                .setType(ServerEvent.Type.Turn)
                .setArguments(new String[]{currentPlayer.getUUID().toString()});

        GamePlay.getInstance().broadcast(event);

        ServerEvent commandsEvent = new ServerEvent()
                .setType(ServerEvent.Type.Commands)
                .setArguments(deal.getState().getAvailableCommands());

        currentPlayer.sendMessage(commandsEvent);
    }

    public void onClientDisconnected(IPlayer client) {
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            //TODO: Zastapic botem

            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientDisconnect)
                    .setArguments(new String[] {client.getUUID().toString()});

            broadcastExcept(client, event);
        }
    }
}
