package lubiezurek.texasholdem.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import org.java_websocket.WebSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class ServerClientThreadTest {

    private ServerClientThread client;


    @Before
    public void setUp() throws Exception {
        client = createClient();
    }

    @After
    public void tearDown() {
        client = null;
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSocketNull() throws Exception {
        new ServerClientThread(Server.getInstance(),null);
    }

    @Test (expected  = IllegalArgumentException.class)
    public void testServerNull() throws Exception {
        WebSocket socket = mock(WebSocket.class);
        new ServerClientThread(null, socket);
    }

    private ServerClientThread  createClient() throws Exception {
        WebSocket socket = mock(WebSocket.class);
        return new ServerClientThread(Server.getInstance(), socket);
    }

    @Test
    public void testUUID() throws Exception {
        ArrayList<UUID> array = new ArrayList<>();
        array.add(client.getUUID());
        for(int i = 1; i < 100; i++) {
            client = createClient();
            array.add(client.getUUID());
        }

        for(int i = 0; i < 100; i++) {
            for(int j = i+1; j < 100; j++) {
                assertNotEquals(array.get(i), array.get(j));
            }
        }
    }


    @Test
    public void testMoney() throws  Exception {
        client.setMoney(1234);
        assertEquals(1234, client.getMoney());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNegativeMoney() throws Exception {
        client.setMoney(-1);
    }

}
