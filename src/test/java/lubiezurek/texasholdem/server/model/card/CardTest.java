package lubiezurek.texasholdem.server.model.card;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
@RunWith(Parameterized.class)
public class CardTest {

    private Suit suit;
    private CardValue value;
    private Card card;

    public CardTest(Suit suit, CardValue value) {
        this.suit = suit;
        this.value = value;

        this.card = new Card(suit, value);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object[]> list = new ArrayList<>();
        for(Suit suit: Suit.values()) {
            for(CardValue value: CardValue.values()) {
                list.add(new Object[] {suit, value});
            }
        }
        return list;
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullSuit() {
        card = new Card(null, value);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullCardValue() {
        card = new Card(suit, null);
    }

    @Test
    public void testGetSuit() throws Exception {
        assertEquals(suit, card.getSuit());
    }

    @Test
    public void testGetCardValue() throws Exception {
        assertEquals(value, card.getCardValue());
    }
}