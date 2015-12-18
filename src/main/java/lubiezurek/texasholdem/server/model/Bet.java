package main.java.lubiezurek.texasholdem.server.model.bet;

import java.lang.Comparable;
import player.IPlayer;

public class Bet implements Comparable{
	private IPlayer player;
	private float amount;

	public Bet(IPlayer player, float amount){
		this.player = player;
		this.amount = amount;
	}

	public IPlayer getPlayer(){
		return player;
	}

	public float getAmount(){
		return amount;
	}
}