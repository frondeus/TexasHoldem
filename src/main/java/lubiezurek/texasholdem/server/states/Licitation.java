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
public abstract class Licitation implements IState {
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
        //Todo: if starting player broke, turn to next until one not 
        //          broke found, or if no players have money go to next state
        biggestBet = 0;
        deal = GamePlay.getInstance().getDeal();
    }

    @Override
    public String[] getAvailableCommands(IPlayer forPlayer) {
        if(forPlayer.getPlayerState() == PlayerState.TURN)
            return new String[] {"Bet", "Check", "Fold",
                                 "GetRequiredBet", "GetPot", 
                                 "GetLicitationType"};
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
                
                if(!betIsFair(player, betValue)){
                    player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Bet command: bad amount"));
                    break;
                }

                makeBet(player, betValue);

                break;

            case "Check":
                if( != ) //TODO if need to call give response: bad command
                //TODO: else: next player
                //      check if licitation should end
                break;

            case "Fold":
                //TODO: tell Client class that player folded
                break;

            case "GetRequiredBet":
                //TODO: tell Client what is the current state of the pot
                break;

            case "GetLicitationType":
                player.sendMessage(new ServerResponse(ServerResponse.Status.Ok,
                        this.getLicitationType()));
                break;
            case "GetRequiredBet":
                break;
            default:
                player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Invalid command"));
                break;
        }
    }

    /*TODO in subclasses: check if bet is fair according to licitation type
                check if betValue > bigBlind
                check if player has enough money to bet betValue
                check if betValue+playerBets => biggestBet
    */
    public abstract boolean betIsFair(IPlayer player, int betValue);

    /*TODO in subclasses: return information about licitation type*/
    public abstract String getLicitationType();
    
    public void makeBet(IPlayer player, int betValue){
        /*TODO:
                add bet to deal
                take away money from player
        */
    }


}
