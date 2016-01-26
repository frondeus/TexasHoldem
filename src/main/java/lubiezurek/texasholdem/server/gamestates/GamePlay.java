package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;
import lubiezurek.texasholdem.server.states.Licitation;
import org.mockito.internal.matchers.Null;

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
    private IPlayer dealer = null;

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

            dealer = players.get(0);
            if(deal == null) deal = new Deal(); //Sprawdzanie nulla do testow
            deal.setUp();
            deal.setState(Licitation.getInstance());
        }
    }

    @Override
    public void changeState() {
        //TODO: Sprawdzanie czy koniec partii czy nie.
    }

    public void nextDealer() {
        dealer = dealer.getNextPlayer();
    }

    public void onClientConnected(IPlayer client) {
        client.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                "Game is in progress"));
        client.disconnect();
    }

    public void onClientMessage(IPlayer client, ClientMessage message) {
        if(!isPlayerTurn(client)) {
            client.sendMessage(new ServerResponse("It's not your turn"));
            return;
        }

        String[] availableCommands = deal.getState().getAvailableCommands();
        for(String s: availableCommands) if(s.equals(message.getCommand())) {
            deal.getState().onPlayerMessage(client, message);
            return;
        }

        client.sendMessage(new ServerResponse("Unknown command"));
    }

    private boolean isPlayerTurn(IPlayer player) {
        return deal.getState().isPlayerTurn(player);
    }

    public void onClientDisconnected(IPlayer client) {
        Logger.status(client + ": Disconnected");
        if(players.indexOf(client) >= 0) {
            //TODO: Zastapic botem
            players.remove(client);

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

    public IPlayer getDealer() {
        return dealer;
    }
}
