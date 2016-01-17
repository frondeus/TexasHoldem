package main.java.lubiezurek.texasholdem.server.model;

import main.java.lubiezurek.texasholdem.server.IPlayer;
import java.lang.Comparable;

public class Bet{
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