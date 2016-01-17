package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by frondeus on 27.12.2015.
 */
public class LobbyTest {

    @Before
    public void setUp() {
        Lobby.resetInstance();
        Server.getInstance().setState(Lobby.getInstance());
    }

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(Lobby.getInstance());
    }

    private PlayerMock createClient() throws Exception {
        return new PlayerMock();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testOnNullClientConnected() throws Exception {
        Lobby.getInstance().onClientConnected(null);
    }

    @Test
    public void testOnClientConnected() throws Exception {

        PlayerMock lastPlayer = null, client = null;
        Lobby.getInstance().maxPlayerCount = 16;

        ServerMessage[] messages = null;
        ServerMessage[] lastPlayerMessages = null;

        for(int i = 0; i < Lobby.getInstance().maxPlayerCount; i++) {
            int oldPlayerCount = Lobby.getInstance().getPlayersCount();

            if(client != null) lastPlayer = client;
            client = createClient();
            Lobby.getInstance().onClientConnected(client);

            assertEquals(1, Lobby.getInstance().getPlayersCount() - oldPlayerCount);
            assertEquals(Lobby.getInstance().startMoney, client.getMoney());
            assertEquals(false, client.isDisconnected());

            messages = client.getLastMessages();
            if(i < Lobby.getInstance().maxPlayerCount - 1)
                assertEquals(1, messages.length);
            else
                assertEquals(2, messages.length);
            assertEquals(ServerResponse.class, messages[0].getClass());
            assertEquals(ServerResponse.Status.Ok, ((ServerResponse)messages[0]).getStatus());
            assertEquals("Welcome", ((ServerResponse)messages[0]).getMessage());

            if(lastPlayer != null) {
                lastPlayerMessages = lastPlayer.getLastMessages();
                if(i < Lobby.getInstance().maxPlayerCount - 1)
                    assertEquals(1, lastPlayerMessages.length);
                else
                    assertEquals(2, lastPlayerMessages.length);
                assertEquals(ServerEvent.class, lastPlayerMessages[0].getClass());

                assertEquals(ServerEvent.Type.ClientConnect, ((ServerEvent)lastPlayerMessages[0]).getEventType());
                assertEquals(1, ((ServerEvent)lastPlayerMessages[0]).getArguments().length);
                assertEquals(client.getUUID().toString(), ((ServerEvent)lastPlayerMessages[0]).getArguments()[0]);
            }
        }
        //Ostatni gracz

        assertEquals(Lobby.getInstance().maxPlayerCount,GamePlay.getInstance().getPlayersCount());
        assertEquals(GamePlay.getInstance(), Server.getInstance().getState());

        assertEquals(ServerEvent.class, messages[1].getClass());
        assertEquals(ServerEvent.Type.ChangeState, ((ServerEvent)messages[1]).getEventType());
        assertEquals(1, ((ServerEvent)messages[1]).getArguments().length);
        assertEquals("GamePlay", ((ServerEvent)messages[1]).getArguments()[0]);

        assertEquals(ServerEvent.class, lastPlayerMessages[1].getClass());
        assertEquals(ServerEvent.Type.ChangeState, ((ServerEvent)lastPlayerMessages[1]).getEventType());
        assertEquals(1, ((ServerEvent)lastPlayerMessages[1]).getArguments().length);
        assertEquals("GamePlay", ((ServerEvent)lastPlayerMessages[1]).getArguments()[0]);

    }

    @Test
    public void testOnClientChat() throws Exception {
        PlayerMock client = createClient();
        Lobby.getInstance().onClientConnected(client);
        assertEquals(false, client.isDisconnected());
        client.getLastMessages();

        ClientMessage message = new ClientMessage()
                .setCommand("chat")
                .setArguments(new String[] { "Hello", "world!" });

        Lobby.getInstance().onClientMessage(client, message);

        ServerMessage[] messages = client.getLastMessages();

        assertEquals(2, messages.length);
        assertEquals(ServerResponse.class, messages[0].getClass());
        assertEquals(ServerResponse.Status.Ok, ((ServerResponse)messages[0]).getStatus());
        assertEquals("Send", ((ServerResponse)messages[0]).getMessage());


        assertEquals(ServerEvent.class, messages[1].getClass());
        assertEquals(ServerEvent.Type.Chat, ((ServerEvent)messages[1]).getEventType());
        assertEquals(3, ((ServerEvent)messages[1]).getArguments().length);
        assertEquals(client.getUUID().toString(), ((ServerEvent)messages[1]).getArguments()[0]);
        assertEquals("Hello", ((ServerEvent)messages[1]).getArguments()[1]);
        assertEquals("world!", ((ServerEvent)messages[1]).getArguments()[2]);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnClientNullMessage() throws Exception {
        PlayerMock client = createClient();
        Lobby.getInstance().onClientConnected(client);
        client.getLastMessages();

        Lobby.getInstance().onClientMessage(client, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnClientNullPlayerMessage() throws Exception {

        ClientMessage message = new ClientMessage()
                .setCommand("foo")
                .setArguments(new String[] { "Hello", "world!" });

        Lobby.getInstance().onClientMessage(null, message);
    }

    @Test
    public void testOnClientNotConnectedMessage() throws  Exception {
        PlayerMock client = createClient();

        ClientMessage message = new ClientMessage()
                .setCommand("foo")
                .setArguments(new String[] { "Hello", "world!" });

        Lobby.getInstance().onClientMessage(client, message);

        ServerMessage[] messages = client.getLastMessages();

        assertEquals(1, messages.length);
        assertEquals(ServerResponse.class, messages[0].getClass());
        assertEquals(ServerResponse.Status.Failure, ((ServerResponse)messages[0]).getStatus());
        assertEquals("Not connected", ((ServerResponse)messages[0]).getMessage());
    }

    @Test
    public void testOnClientInvalidCommand() throws Exception {
        PlayerMock client = createClient();
        Lobby.getInstance().onClientConnected(client);
        client.getLastMessages();

        ClientMessage message = new ClientMessage()
                .setCommand("foo")
                .setArguments(new String[] { "Hello", "world!" });

        Lobby.getInstance().onClientMessage(client, message);

        ServerMessage[] messages = client.getLastMessages();

        assertEquals(1, messages.length);
        assertEquals(ServerResponse.class, messages[0].getClass());
        assertEquals(ServerResponse.Status.Failure, ((ServerResponse)messages[0]).getStatus());
        assertEquals("Invalid command", ((ServerResponse)messages[0]).getMessage());
    }


    @Test
    public void testOnClientDisconnected() throws Exception {
        PlayerMock lastClient = createClient();
        Lobby.getInstance().onClientConnected(lastClient);

        PlayerMock client = createClient();
        Lobby.getInstance().onClientConnected(client);
        lastClient.getLastMessages();
        client.getLastMessages();

        Lobby.getInstance().onClientDisconnected(client);

        ServerMessage[] messages = lastClient.getLastMessages();

        assertEquals(1, messages.length);
        assertEquals(ServerEvent.class, messages[0].getClass());
        assertEquals(ServerEvent.Type.ClientDisconnect, ((ServerEvent)messages[0]).getEventType());
        assertEquals(1, ((ServerEvent)messages[0]).getArguments().length);
        assertEquals(client.getUUID().toString(), ((ServerEvent)messages[0]).getArguments()[0]);
    }
}