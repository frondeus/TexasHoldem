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
import lubiezurek.texasholdem.server.ServerResponse;
import lubiezurek.texasholdem.server.PlayerState;

import java.util.ArrayList;

/**
 * Created by frondeus on 23.01.16.
 */
public abstract class Licitation implements IState {
    private Deal deal = null;
    private int biggestBet;

    public Licitation() {
    }

    @Override
    public void onStart(Deal deal) {
        //Todo: if starting player broke, turn to next until one not 
        //          broke found, or if no players have money go to next state
        biggestBet = 0;
        this.deal = deal;
    }

    @Override
    public String[] getAvailableCommands(IPlayer forPlayer) {
        if(forPlayer.getPlayerState() == PlayerState.TURN)
            return new String[] {"Bet", "Check", "Fold",
                                 "GetRequiredBet", "GetPot", 
                                 "GetLicitationType"};
        else return new String[] {"GetRequiredBet", "GetPot", 
                                 "GetLicitationType"};
    }

    @Override
    public boolean isPlayerTurn(IPlayer player) {
        //TODO
        return true;
    }

    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {
        Logger.status("Command: " + message.getCommand());

        if(player == null) throw new IllegalArgumentException();
        if(message == null) throw new IllegalArgumentException();

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
                    betValue = Integer.parseInt(message.getArguments()[0]);
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

                deal.addBet(player, betValue);
                //TODO: calculate biggest bet
                //TODO: switch state to next player, notify next player that it's his turn
                break;

            case "Check":
                //if( != ) //TODO if need to call give response: bad command
                //TODO: else: next player
                //      check if licitation should end
                break;

            case "Fold":
                player.setPlayerState(PlayerState.FOLD);
                break;

            case "GetPot":
                //TODO: tell Client what is the current state of the pot
                break;

            case "GetLicitationType":
                player.sendMessage(new ServerResponse(ServerResponse.Status.Ok,
                        this.getLicitationType()));
                break;

            case "GetRequiredBet":
                //TOFO: tell Client the required amount to call
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
}
