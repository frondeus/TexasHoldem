package lubiezurek.texasholdem.server;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 21.12.2015.
 */
public class ServerResponseTest {

    private ServerResponse createResponse() {
        return new ServerResponse("Foo");
    }

    @Test
    public void testStatus() throws Exception {

        ServerResponse response = new ServerResponse(ServerResponse.Status.Failure, "");
        assertEquals(ServerResponse.Status.Failure, response.getStatus());

        response = new ServerResponse(ServerResponse.Status.Ok, "");
        assertEquals(ServerResponse.Status.Ok, response.getStatus());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullStatus() {
        new ServerResponse(null, "");
    }

    @Test
    public void testMessage() throws Exception {
        ServerResponse response = new ServerResponse("Testowy string");
        assertEquals("Testowy string", response.getMessage());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullMessage() {
        ServerResponse response = new ServerResponse(ServerResponse.Status.Failure, null);
    }
}