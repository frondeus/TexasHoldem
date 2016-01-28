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
	ArrayList<Bet> betsFromEach; 
	private IPlayer dealer = null;
    private Card[] flop = new Card[3];
    private Card turn = null;
    private Card river = null;

    public Deal(){
        ArrayList<IPlayer> players = GamePlay.getInstance().getPlayers();
        int playerAmount = players.size();
		betsFromEach = new ArrayList<Bet>(playerAmount);
        for (int i = 0; i < playerAmount; ++i) {
            betsFromEach[i] = new Bet(players[i], 0);
        }
	}

    public void start() {
        //TODO: giving blinds (maybe should be in another state?)
        for (IPlayer player : GamePlay.getInstance().getPlayers()){
            player.setPlayerState(PlayerState.WAITING);
        }
    }


	public IState getState(){ return currentState; }

	public void setState(IState newState){
        currentState = newState;

        GamePlay.getInstance().broadcast(new ServerEvent(ServerEvent.Type.ChangeState, new String[]{
                        newState.getClass().getSimpleName()
                        })
        );


        currentState.onStart(this);
    }

    public void addBet(int betToAdd, IPlayer fromPlayer) {
        int playerIndex = GamePlay.getInstance().getPlayers().indexOf(fromPlayer);
        if(playerIndex == -1) throw new IllegalArgumentException();

        betsFromEach[playerIndex].addAmount(betToAdd);
    }
}
