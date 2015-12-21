package lubiezurek.texasholdem.server.model.card;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class CardValueTest {

    @Test
    public void testGetValue() throws Exception {
        assertEquals(2, CardValue.TWO.getValue());
        assertEquals(6, CardValue.SIX.getValue());
        assertEquals(11, CardValue.JACK.getValue());
        assertEquals(12, CardValue.QUEEN.getValue());
        assertEquals(14, CardValue.ACE.getValue());
    }
}