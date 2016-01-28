package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.gamestates.Lobby;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;

import java.util.ArrayList;

/**
 * Created by frondeus on 23.01.16.
 */
public class Licitation implements IState {
    private volatile  static Licitation instance;
    public static Licitation getInstance() {
        if(instance == null) {
            synchronized(Licitation.class) {
                if(instance == null) instance = new Licitation();
            }
        }
        return instance;
    }

    public static void resetInstance() {
        synchronized (Licitation.class) {
            instance = null;
        }
    }

    private Deal deal = NULL;
    private int biggestBet;

    private Licitation() {
    }

    @Override
    public void onStart() {
        biggestBet = 0;
        deal = GamePlay.getInstance().getDeal();
    }

    @Override
    public String[] getAvailableCommands() {
        return new String[] {"Bet", "Check", "Fold"};
    }

    @Override
    public boolean isPlayerTurn(IPlayer player) {
        //TODO
    }

    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {
        Logger.status("Command: " + message.getCommand());

        if(client == null) throw new IllegalArgumentException();
        if(message == null) throw  new IllegalArgumentException();

        if(!isPlayerTurn(player)){
            player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                "Bad command: not your turn"));
            return;
        }


        //TODO: broadcast the moves
        switch(message.getCommand()){
            case "Bet":
                int betValue;
                try{
                    betValue = Integer.parseInt(message.getArguments()[]);
                }
                catch(NumberFormatException ex){
                    player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Bet command: argument invalid"));
                    break;
                }
                /*
                TODO: add Bet to Deal
                      check if player has enough money to bet betValue
                      check if betValue+playerBets => biggestBet
                      take away money from player
                      next state (?) - check if player is the last one to 
                */


                break;
            case "Check":
                if(biggestBet != 0) //TODO give response: bad command
                //TODO: else: next player
                break;

            case "Fold":
                //TODO: tell 
                //TODO: tell the deal class that the player folded.
                break;
            default:
                client.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Invalid command"));
                break;
        }
    }
}
