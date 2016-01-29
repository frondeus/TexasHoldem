package lubiezurek.texasholdem.server.deal;


import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;
import lubiezurek.texasholdem.server.states.Licitation;

import java.util.ArrayList;

public class Deal{
	private IState currentState = null;
	private ArrayList<Bet> bets = new ArrayList<Bet>();
    private Card[] flop = new Card[3];
    private Card turn = null;
    private Card river = null;
    private Deck deck = null;

    public Deal(){}

    public void start(IPlayer dealer) {
        for (IPlayer p : GamePlay.getInstance().getPlayers()) {
            p.setPlayerState(PlayerState.WAITING);
        }

        //TODO: set first state to handShuffle, generate cards, then set state to Licitation
        setState(GamePlay.getInstance().getLicitationState());

        Licitation licitation = (Licitation) currentState;

        IPlayer smallBlind = switchToNextPlayerFrom(dealer);
        licitation.makeBet(smallBlind, Server.getInstance().Options.getSmallBlind());

        IPlayer bigBlind = switchToNextPlayerFrom(smallBlind);
        licitation.makeBet(bigBlind, Server.getInstance().Options.getBigBlind());

        switchToNextPlayerFrom(bigBlind);
        notifyPlayerTurn();


        if(deck == null) deck = new Deck(); // Nie twórz na unit testach
        deck.shuffle();
    }

    public IPlayer switchToNextPlayerFrom(IPlayer currentPlayer){
        if(currentPlayer == null) throw new IllegalArgumentException();

        currentPlayer.setPlayerState(PlayerState.WAITING);
        currentPlayer.getNextPlayer().setPlayerState(PlayerState.TURN);

        return currentPlayer.getNextPlayer();
    }

    public void notifyPlayerTurn(){
        for (IPlayer p : GamePlay.getInstance().getPlayers()) {
            if(p.getPlayerState() == PlayerState.TURN){
                Logger.status("Notifying player that it's his turn");
                GamePlay.getInstance().broadcast(
                        new ServerEvent(ServerEvent.Type.Turn,
                            new String[] {p.getUUID().toString()}));
                break;
            }

        }
    }

    public void addBet(IPlayer player, int amount) {
        int moneyLeft = player.getMoney();

        player.takeAwayMoney(amount);

        Bet bet = new Bet(player, Math.min(amount, moneyLeft));
        bets.add(bet);

        GamePlay.getInstance().broadcast(new ServerEvent(
                ServerEvent.Type.Bet,
                new String[] {
                        player.getUUID().toString(),
                        Integer.toString(player.getMoney()),
                        Integer.toString(sumBetAmount(player))
                }
        ));
    }

    public int sumBetAmount(IPlayer player) {
        int sum = 0;
        for(Bet bet: bets)
            if(bet.getPlayer() == player) sum += bet.getAmount();
        return sum;
    }

    public int playersStillInPlay(){
        int playerAmount = 0;
        for (IPlayer p : GamePlay.getInstance().getPlayers()) {
            if(p.getPlayerState() == PlayerState.WAITING
                    || p.getPlayerState() == PlayerState.TURN){
                playerAmount += 1;
            }
        }
        return playerAmount;
    }

    public int getPotAmount(){
        int sum = 0;
        for (Bet bet: bets) {
            sum += bet.getAmount();
        }
        return sum;
    }

	public void setState(IState newState){
        currentState = newState;

        GamePlay.getInstance().broadcast(new ServerEvent(ServerEvent.Type.ChangeState, new String[]{
                        newState.getClass().getSimpleName()
                        })
        );

        currentState.onStart(this);
    }

    public IState getState(){ return currentState; }

    public ArrayList<Bet> getBets() { return bets; }

    public void setDeck(Deck deck) { this.deck = deck; }
}
