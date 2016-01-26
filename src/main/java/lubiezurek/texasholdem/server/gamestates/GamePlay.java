package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;
import lubiezurek.texasholdem.server.states.Licitation;

import java.util.Collections;

/**
 * Created by frondeus on 14.12.2015.
 */
public class GamePlay extends GameState {
    private volatile static GamePlay instance;
    public static GamePlay getInstance() {
        if(instance == null) {
            synchronized (GamePlay.class) {
                if(instance == null) instance = new GamePlay();
            }
        }
        return instance;
    }
    public static void resetInstance() {
        instance = null;
    }

    private Deal deal = null;
    private IState licitationState = null;

    private GamePlay() {
    }

    private void createQueue() {
        Collections.shuffle(players);

        IPlayer player = null, lastPlayer = null;
        for(int i = 0; i < players.size(); i++) {
            if(player != null) lastPlayer = player;
            player = players.get(i);
            player.setNextPlayer(players.get(0));
            if(lastPlayer != null)
                lastPlayer.setNextPlayer(player);
        }
    }

    public void onEnter() {
        //TODO:
        licitationState = Licitation.getInstance();
        if(players.size() > 0 ) {
            createQueue();

            Logger.status("Deal: " + deal);
            if(deal == null) deal = new Deal(); //Sprawdzanie nulla do testow
            deal.setDealer(players.get(0));
            deal.setUp();
            deal.setState(Licitation.getInstance());
        }
    }

    @Override
    public void changeState() {

    }


    public void onClientConnected(IPlayer client) {
        client.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                "Game is in progress"));
        client.disconnect();
    }

    public void onClientMessage(IPlayer client, ClientMessage message) {
        /*
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
            client.sendMessage(new ServerResponse("Unknown command"));
        }
        else {
            client.sendMessage(new ServerResponse("It's not your turn"));
        }
        */
    }

    /*
    public void sendTurnEvent() {
        IPlayer currentPlayer = deal.getCurrentPlayer();

        broadcast(new ServerEvent(ServerEvent.Type.Turn,
                new String[]{ currentPlayer.getUUID().toString() }));


        currentPlayer.sendMessage(new ServerEvent(
                ServerEvent.Type.Commands,
                deal.getState().getAvailableCommands()
        ));

    }

    public void sendHand(IPlayer player, Card first, Card second) {
        player.sendMessage( new ServerEvent(
                ServerEvent.Type.Hand,
                new String[] {
                        first.getSuit().toString(),
                        first.getCardValue().toString(),
                        second.getSuit().toString(),
                        second.getCardValue().toString()
                }
        ));
    }

    public void sendSharedCard(Card card) {
        broadcast(new ServerEvent(
                ServerEvent.Type.SharedCard,
                new String[]{
                        card.getSuit().toString(),
                        card.getCardValue().toString()
                }
        ));
    }

    public void sendOtherHand(IPlayer player, IPlayer other, Card first, Card second) {
        player.sendMessage(new ServerEvent(
                ServerEvent.Type.OtherHand,
                new String[] {
                        other.getUUID().toString(),
                        first.getSuit().toString(),
                        first.getCardValue().toString(),
                        second.getSuit().toString(),
                        second.getCardValue().toString()
                }
        ));
    }
*/
    public void onClientDisconnected(IPlayer client) {
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            //TODO: Zastapic botem

            broadcastExcept(client, new ServerEvent(
                    ServerEvent.Type.ClientDisconnect,
                    new String[] {
                            client.getUUID().toString()
                    }
            ));

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

    public IState getLicitationState() {
        return licitationState;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }
}
