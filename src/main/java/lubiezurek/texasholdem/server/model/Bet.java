package main.java.lubiezurek.texasholdem.server.model;

import lubiezurek.texasholdem.server.IPlayer;
import java.lang.Comparable;

public class Bet{
	private IPlayer player;
	private int amount;

	public Bet(IPlayer player, int amount){
		this.player = player;
		this.amount = amount;
	}

	public IPlayer getPlayer(){
		return player;
	}

	public int getAmount(){
		return amount;
	}

	public void addAmount(int amount){
		this.amount += amount;
	}
}
