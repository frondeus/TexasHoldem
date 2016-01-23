package lubiezurek.texasholdem.server.deal;


import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.model.card.Card;

import java.util.ArrayList;

public class Deal{
	private IState currentState;
	//ArrayList<Bet> bets = new ArrayList<Bet>();
	private ArrayList<Card> cards = new ArrayList<Card>();
	private IPlayer currentPlayer;

	public Deal(){
		
	}

    public void setFirstPlayer(IPlayer player) {
        currentPlayer = player;
    }

    public void nextPlayer() {
        currentPlayer = currentPlayer.getNextPlayer();
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



}
