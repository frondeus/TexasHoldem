package lubiezurek.texasholdem.server.model;

import lubiezurek.texasholdem.server.model.card.*;
import java.util.ArrayList;
import java.util.Collections;

public class Deck{
	private ArrayList<Card> cards;
	
	public final static int DEFAULT_DECK_WIDTH = 52;
	public final static int CARDS_PER_SUIT = 13;
	public Deck() {
		reset();
	}

	public void reset(){
		cards = new ArrayList<Card>();
		Suit[] cardSuits = {Suit.HEARTS,
							Suit.SPADES,
							Suit.CLUBS,
							Suit.DIAMONDS};

		CardValue[] cardValues = {
							CardValue.TWO,
							CardValue.THREE,
							CardValue.FOUR,
							CardValue.FIVE,
							CardValue.SIX,
							CardValue.SEVEN,
							CardValue.EIGHT,
							CardValue.NINE,
							CardValue.TEN,
							CardValue.JACK,
							CardValue.QUEEN,
							CardValue.KING,
							CardValue.ACE};

		for (int i = 0; i < DEFAULT_DECK_WIDTH; i++) {
			cards.add(new Card( cardSuits[i/CARDS_PER_SUIT], 
				 	  			cardValues[i%CARDS_PER_SUIT] ));
		}
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/*public void addCard(Card newCard){
		cards.add(newCard);
	}*/
	
	public void removeCard(int index) throws Exception {
		if(size() - 1 < index || index < 0)  throw new IndexOutOfBoundsException("index")

		cards.remove(index);
	}
	
	public Card drawCard(int index) throws Exception {
		Card cardToDraw = cards.get(index);
		removeCard(index);
		return cardToDraw;
	}
	
	public Card drawLast() throws Exception {
		return drawCard(size() - 1);
	}
	
	public int size(){
		return cards.size();
	}
}