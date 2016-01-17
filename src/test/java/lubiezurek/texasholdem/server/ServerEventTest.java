package lubiezurek.texasholdem.server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class ServerEventTest {

    @Test
    public void testType() {
        ServerEvent event = new ServerEvent();

        event.setType(ServerEvent.Type.Chat);
        assertEquals(ServerEvent.Type.Chat, event.getEventType());

    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullType() {
        ServerEvent event = new ServerEvent();
        event.setType(null);
    }

    @Test
    public void testArguments() {
        ServerEvent event = new ServerEvent();

        event.setArguments(new String[] { "Aaa", "Bbb" });

        String[] args = event.getArguments();
        assertNotNull(args);
        assertEquals("Aaa", args[0]);
        assertEquals("Bbb", args[1]);
        assertEquals(2, args.length);
    }

    @Test
    public void testEmptyArguments() {
        ServerEvent event = new ServerEvent();

        event.setArguments(new String[] {} );

        assertNotNull(event.getArguments());
        assertEquals(0, event.getArguments().length);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullArguments() {
        ServerEvent event = new ServerEvent();
        event.setArguments(null);
    }

}