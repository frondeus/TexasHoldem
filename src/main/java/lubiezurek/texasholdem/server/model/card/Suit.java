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

	@Override
	public String toString() {
		switch (this) {
			case HEARTS:
				return "Hearts";
			case SPADES:
				return "Spades";
			case CLUBS:
				return "Clubs";
			case DIAMONDS:
				return "Diamonds";
		}
		return null;
	}

	public int getValue(){
		return value;
	}
}

