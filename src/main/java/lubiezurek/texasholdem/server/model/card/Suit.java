package lubiezurek.texasholdem.server.model.card;

public enum Suit{
	HEARTS(0),
	SPADES(1),
	CLUBS(2),
	DIAMONDS(3);

	private int value;

	Suit(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}
}

