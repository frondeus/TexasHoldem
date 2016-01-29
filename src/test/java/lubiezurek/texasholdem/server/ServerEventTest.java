package lubiezurek.texasholdem.server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class ServerEventTest {

    private ServerEvent createEvent() {
        return new ServerEvent(
                ServerEvent.Type.Chat,
                new String[] {}
        );
    }
    @Test
    public void testConstructorType() {
        ServerEvent event = new ServerEvent(ServerEvent.Type.Chat,
                new String[] {});

        assertEquals(ServerEvent.Type.Chat, event.getEventType());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullType() {
        new ServerEvent(null, new String[] {} );
    }

    @Test
    public void testConstructorArguments() {
        ServerEvent event = new ServerEvent(ServerEvent.Type.Chat,
                new String[] {"Aaa", "Bbb"});

        String[] args = event.getArguments();
        assertNotNull(args);
        assertEquals("Aaa", args[0]);
        assertEquals("Bbb", args[1]);
        assertEquals(2, args.length);
    }

    @Test
    public void testArguments() {
        ServerEvent event = createEvent();

        event.setArguments(new String[] { "Aaa", "Bbb" });

        String[] args = event.getArguments();
        assertNotNull(args);
        assertEquals("Aaa", args[0]);
        assertEquals("Bbb", args[1]);
        assertEquals(2, args.length);
    }

    @Test
    public void testEmptyArguments() {
        ServerEvent event = createEvent();

        event.setArguments(new String[] {} );

        assertNotNull(event.getArguments());
        assertEquals(0, event.getArguments().length);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullArguments() {
        ServerEvent event = createEvent();
        event.setArguments(null);
    }

}