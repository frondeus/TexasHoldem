package lubiezurek.texasholdem.server.deal;


import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.Server;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;

import java.util.ArrayList;

public class Deal{
	private IState currentState = null;
	private ArrayList<Bet> bets = new ArrayList<Bet>();
    private Card[] flop = new Card[3];
    private Card turn = null;
    private Card river = null;
    private IPlayer smallBlindPlayer = null;
    private IPlayer bigBlindPlayer = null;
    private Deck deck = null;

    public Deal(){}

    public void start(IPlayer dealer) {
        smallBlindPlayer = dealer.getNextPlayer();
        bigBlindPlayer = smallBlindPlayer.getNextPlayer();

        addBet(smallBlindPlayer, Server.getInstance().Options.getSmallBlind());
        addBet(bigBlindPlayer, Server.getInstance().Options.getBigBlind());

        if(deck == null) deck = new Deck(); // Nie twórz na unit testach
        deck.shuffle();

        setState(GamePlay.getInstance().getLicitationState());
    }

    public void addBet(IPlayer player, int amount) {
        int moneyLeft = availableMoney(player);

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

    public int availableMoney(IPlayer player) {
        return player.getMoney() - sumBetAmount(player);
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

    public IPlayer getBigBlindPlayer() { return bigBlindPlayer; }

    public IPlayer getSmallBlindPlayer() { return smallBlindPlayer; }

    public ArrayList<Bet> getBets() { return bets; }

    public void setDeck(Deck deck) { this.deck = deck; }
}
