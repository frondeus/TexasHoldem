package lubiezurek.texasholdem.server.deal;


import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;

import java.util.ArrayList;

public class Deal{
	private IState currentState;
	//ArrayList<Bet> bets = new ArrayList<Bet>();
	private IPlayer currentPlayer;
    private Card[] flop = new Card[3];
    private Card turn = null;
    private Card river = null;

    public Deal(){
		
	}

    public void setPlayer(IPlayer player) {
        currentPlayer = player;
        GamePlay.getInstance().sendTurnEvent();
    }

    public void nextPlayer() {
        if(currentPlayer == null) Logger.error("There is no player!. Error");
        if(currentPlayer.getNextPlayer() == null) Logger.error("There is no next player. Error!");
        setPlayer(currentPlayer.getNextPlayer());
    }

    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

	public IState getState(){ return currentState; }

	public void setState(IState newState){
        currentState = newState;

        ServerEvent event = new ServerEvent()
                .setType(ServerEvent.Type.ChangeState)
                .setArguments(new String[] {newState.getClass().getSimpleName()});
        GamePlay.getInstance().broadcast(event);

        currentState.onStart();
    }


    public void setFlop(Card[] flop) {
        this.flop = flop;
    }
}
