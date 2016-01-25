package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;
import lubiezurek.texasholdem.server.states.Licitation;

import java.util.ArrayList;
import java.util.Arrays;
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
        if(currentPlayer == client) {
            String[] availableCommands = deal.getState().getAvailableCommands();
            for(String s: availableCommands) {
                if(s.equals(message.getCommand()))
                {
                    deal.getState().onPlayerMessage(client, message);
                    return;
                }
            }
            ServerResponse response = new ServerResponse()
                    .setStatus(ServerResponse.Status.Failure)
                    .setMessage("Unknown command");
            client.sendMessage(response);
        }
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

    public void sendHand(IPlayer player, Card first, Card second) {
        ServerEvent event = new ServerEvent()
                .setType(ServerEvent.Type.Hand)
                .setArguments(new String[]{
                        first.getSuit().toString(),
                        first.getCardValue().toString(),
                        second.getSuit().toString(),
                        second.getCardValue().toString()
                });

        player.sendMessage(event);
    }

    public void sendSharedCard(Card card) {
        ServerEvent event = new ServerEvent()
                .setType(ServerEvent.Type.SharedCard)
                .setArguments(new String[]{
                        card.getSuit().toString(),
                        card.getCardValue().toString()
                });

        broadcast(event);
    }

    public void onClientDisconnected(IPlayer client) {
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            //TODO: Zastapic botem

            ServerEvent event = new ServerEvent()
                    .setType(ServerEvent.Type.ClientDisconnect)
                    .setArguments(new String[] {client.getUUID().toString()});

            broadcastExcept(client, event);

            for(IPlayer all : players) {
                if(all.getNextPlayer() == client)
                    all.setNextPlayer(client.getNextPlayer());
            }
            players.remove(client);
            if(players.size() <= 1) {
                Lobby.getInstance().setPlayers(players);
                Server.getInstance().setState(Lobby.getInstance());
            }
        }
    }

    public Deck getDeck() {
        return deck;
    }
}
