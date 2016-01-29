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

public abstract class Licitation implements IState {
    protected Deal deal = null;
    protected int biggestBet;
    protected IPlayer bigFish = null;

    public Licitation() {
    }

    @Override
    public void onStart(Deal deal) {
        biggestBet = 0;
        this.deal = deal;
        if(deal.playersStillInPlay() < 2) {
            IState nextState = new Shuffle();
            deal.setState(nextState);
        }
    }

    @Override
    public String[] getAvailableCommands(IPlayer forPlayer) {
        if(forPlayer.getPlayerState() == PlayerState.TURN)
            return new String[] {"Bet", "Check", "Fold",
                                 "GetRequiredBet", "GetPot", 
                                 "GetLicitationType", "GetTurn"};
        else return new String[] {"GetRequiredBet", "GetPot", 
                                 "GetLicitationType", "GetTurn"};
    }

    @Override
    public boolean isPlayerTurn(IPlayer player) {
        if(player.getPlayerState() == PlayerState.TURN) return true;
        else return false;
    }

    public boolean isValidCommand(String command, IPlayer player){
        String[] availableCommands = getAvailableCommands(player);
        for(String s: availableCommands) {
            if (s.equals(command)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {
        if(player == null) throw new IllegalArgumentException();
        if(message == null) throw new IllegalArgumentException();

        Logger.status("Command: " + message.getCommand());

        if(!isValidCommand(message.getCommand(), player)){
            player.sendMessage(new ServerResponse("Bad command"));
            return;
        }

        switch(message.getCommand()){
            case "Bet":
                int betValue;
                try{
                    betValue = Integer.parseInt(message.getArguments()[0]);
                }
                catch(NumberFormatException ex){
                    player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Bet: argument invalid"));
                    break;
                }
                
                if(!betIsFair(player, betValue)){
                    player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Bet: bad amount"));
                    break;
                }

                //changes bigFish reference if required
                makeBet(player, betValue);

                deal.switchToNextPlayerFrom(player);
                deal.notifyPlayerTurn();

                //check if licitation should end
                if(player.getNextPlayer() == bigFish) deal.setState(new Shuffle());
                break;

            case "Check":
                if(deal.sumBetAmount(player) != biggestBet){
                    player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                            "Cannot check: unsuficient bets"));
                    return;
                }
                deal.switchToNextPlayerFrom(player);
                deal.notifyPlayerTurn();
                //TODO:      check if licitation should end
                break;

            case "Fold":
                player.setPlayerState(PlayerState.FOLD);
                GamePlay.getInstance().broadcast(
                        new ServerEvent(ServerEvent.Type.Fold,
                        new String[] {player.getUUID().toString()}
                ));
                deal.switchToNextPlayerFrom(player);
                deal.notifyPlayerTurn();
                //TODO:      check if licitation should end
                break;

            case "GetPot":
                player.sendMessage(
                        new ServerEvent(ServerEvent.Type.PotAmount,
                        new String[] {Integer.toString(this.deal.getPotAmount())}
                ));
                break;

            case "GetLicitationType":
                player.sendMessage(
                            new ServerEvent(ServerEvent.Type.LicitationType,
                            new String[] {getLicitationType()}
                        ));
                break;

            case "GetRequiredBet":
                player.sendMessage(
                        new ServerEvent(ServerEvent.Type.RequiredBet,
                        new String[] {Integer.toString(getRequiredBet(player))}
                ));
                break;

            case "GetTurn":
                player.sendMessage(
                        new ServerEvent(ServerEvent.Type.Turn,
                        new String[] {player.getUUID().toString()}
                ));
                break;

            default:
                player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                        "Command avaible, but not implemented yet"));
                break;
        }
    }

    public void makeBet(IPlayer player, int betValue){
        deal.addBet(player, betValue);
        if(player.getMoney() == 0) player.setPlayerState(PlayerState.BROKE);

        if(biggestBet < deal.sumBetAmount(player) + betValue){
            bigFish = player;
            biggestBet = deal.sumBetAmount(player) + betValue;
        }

    }

    /*TODO in subclasses: check if bet is fair according to licitation type
                check if betValue > bigBlind
                check if player has enough money to bet betValue
                check if betValue+playerBets => biggestBet
    */
    public abstract boolean betIsFair(IPlayer player, int betValue);
    public abstract int getRequiredBet(IPlayer player);

    /*TODO in subclasses: return information about licitation type*/
    public abstract String getLicitationType();
}
