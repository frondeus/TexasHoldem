package lubiezurek.texasholdem.server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class ServerResponseTest {

    @Test
    public void testStatus() throws Exception {
        ServerResponse response = new ServerResponse();

        response.setStatus(ServerResponse.Status.Failure);
        assertEquals(ServerResponse.Status.Failure, response.getStatus());

        response.setStatus(ServerResponse.Status.Ok);
        assertEquals(ServerResponse.Status.Ok, response.getStatus());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullStatus() {
        ServerResponse response = new ServerResponse();
        response.setStatus(null);
    }

    @Test
    public void testMessage() throws Exception {
        ServerResponse response = new ServerResponse();

        response.setMessage("Testowy string");
        assertEquals("Testowy string", response.getMessage());

    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullMessage() {
        ServerResponse response = new ServerResponse();
        response.setMessage(null);
    }
}