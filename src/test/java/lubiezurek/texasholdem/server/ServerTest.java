package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.server.states.GamePlay;
import lubiezurek.texasholdem.server.states.Lobby;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class ServerTest {


    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(Server.getInstance());
    }

    @Test
    public void testGetClientMessageBuilder() throws Exception {
        assertNotNull(Server.getInstance().getClientMessageBuilder());
    }

    @Test
    public void testGetServerMessageBuilder() throws Exception {
        assertNotNull(Server.getInstance().getServerMessageBuilder());
    }

    @Test (expected = IllegalArgumentException.class, timeout = 100)
    public void testRunNullArguments() throws Exception {
        Server.getInstance().run(null);
    }

    @Test (expected = IllegalArgumentException.class, timeout = 100)
    public void testRunInvalidNumberArguments() throws Exception {
        Server.getInstance().run(new String[] { "-s", "1234", "5678" });
    }

    @Test (expected = NumberFormatException.class, timeout = 100)
    public void testInvalidIntegerArgument() throws Exception {
        Server.getInstance().run(new String[] { "-s", "j123asd" });
    }

    @Test
    public void testState() throws Exception {
        Server.getInstance().setState(GamePlay.getInstance());
        assertEquals(GamePlay.getInstance(), Server.getInstance().getState());
    }
}