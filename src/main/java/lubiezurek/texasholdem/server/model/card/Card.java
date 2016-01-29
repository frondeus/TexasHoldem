package lubiezurek.texasholdem.server.model.card;

public class Card{
	private Suit suit;
	private CardValue value;

	public Card(Suit new_suit, CardValue new_value){
		if(new_suit == null) throw new IllegalArgumentException();
		if(new_value == null) throw new IllegalArgumentException();

		this.suit = new_suit;
		this.value = new_value;
	}

	public Suit getSuit(){
		return suit;
	}

	public CardValue getCardValue(){
		return value;
	}

	@Override
	public String toString(){
        return value.toString() + " " + suit.toString() ;
	}
}