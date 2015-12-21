package lubiezurek.texasholdem.server.model.card;

public class Card{
	private Suit suit;
	private CardValue value;

	public Card(Suit new_suit, CardValue new_value){
		this.suit = new_suit;
		this.value = new_value;
	}

	public Suit getSuit(){
		return suit;
	}

	public CardValue getCardValue(){
		return value;
	}
}