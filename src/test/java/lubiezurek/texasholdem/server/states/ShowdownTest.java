package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.server.PlayerMock;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;
import lubiezurek.texasholdem.server.model.card.CardValue;
import lubiezurek.texasholdem.server.model.card.Suit;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 28.01.16.
 */
public class ShowdownTest {
    private Showdown showdown;

    @Before
    public void setUp() throws Exception {
        showdown = new Showdown();

    }

    @Test
    public void isOnePairShouldEvaluate(){
        PlayerMock player = new PlayerMock();
        player.setHand(new Card[] {
                new Card(Suit.HEARTS, CardValue.TWO),
                new Card(Suit.SPADES, CardValue.THREE),
        });

        Card[] table = new Card[] {
                new Card(Suit.CLUBS, CardValue.ACE),
                new Card(Suit.SPADES, CardValue.ACE),
                new Card(Suit.HEARTS, CardValue.FIVE),
                new Card(Suit.DIAMONDS, CardValue.EIGHT),
                new Card(Suit.HEARTS, CardValue.JACK)
        };

        int ret = showdown.evaluatePlayer(player, table);
        assertEquals(showdown.addToSum[1] + 14+14+11+8+5,ret);

    }
}