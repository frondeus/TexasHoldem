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
    private Card[] flop = new Card[3];
    private Card turn = null;
    private Card river = null;

    public Deal(){
		
	}




	public IState getState(){ return currentState; }

	public void setState(IState newState){
        currentState = newState;

        GamePlay.getInstance().broadcast(
                new ServerEvent(ServerEvent.Type.ChangeState, new String[]{
                        newState.getClass().getSimpleName()
                        })
        );


        currentState.onStart(this);
    }


    public void setFlop(Card[] flop) {
        this.flop = flop;
    }

    public void setUp() {

    }
}
