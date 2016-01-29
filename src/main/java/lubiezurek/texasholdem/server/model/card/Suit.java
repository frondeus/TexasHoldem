package lubiezurek.texasholdem.server.model.card;

public enum Suit{
	HEARTS,
	SPADES,
	CLUBS,
	DIAMONDS;

	@Override
	public String toString() {
		switch(this){
			case HEARTS: return "Hearts";
			case SPADES: return "Spades";
			case CLUBS: return "Clubs";
			case DIAMONDS: return "Diamonds";
		}
		return null;
	}
}

