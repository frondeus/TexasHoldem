package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

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

    private void assertResponse(ServerResponse.Status status, String message, ServerMessage msg) {
        ServerResponse response = (ServerResponse) msg;
        assertEquals(ServerResponse.class, response.getClass());
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
    }

    private void assertEvent(ServerEvent.Type type, String[] args, ServerMessage msg) {
        ServerEvent event = (ServerEvent) msg;

        assertEquals(ServerEvent.class, event.getClass());
        assertEquals(type, event.getEventType());
        assertEquals(args.length, event.getArguments().length);
        for(int i = 0; i < args.length; i++)
            assertEquals(args[i], event.getArguments()[i]);
    }

    @Test
    public void testOnEveryClientConnected() throws Exception {
        Lobby.getInstance().maxPlayerCount = 16;

        int oldPlayerCount;
        ServerMessage[] messages = null;
        ArrayList<String> uuids = new ArrayList<>();

        for(int i = 0; i < Lobby.getInstance().maxPlayerCount - 1; i++) {
            oldPlayerCount = Lobby.getInstance().getPlayersCount();

            PlayerMock client = createClient();
            Lobby.getInstance().onClientConnected(client);

            assertEquals(1, Lobby.getInstance().getPlayersCount() - oldPlayerCount);    //Polaczlo jednego
            assertEquals(Lobby.getInstance().startMoney, client.getMoney());       // Ustawilo mu kase
            assertEquals(false, client.isDisconnected());

            messages = client.getLastMessages();
            assertEquals(3, messages.length);
            assertResponse(ServerResponse.Status.Ok, "Welcome", messages[0]); // Info ze polaczono
            ArrayList<String> uuidsToSend = new ArrayList<>();
            uuidsToSend.add(client.getUUID().toString());
            uuidsToSend.addAll(uuids);

            assertEvent(ServerEvent.Type.Connected, uuidsToSend.toArray(new String[]{}) , messages[1]);
                //Wysylamy info o jego UUID oraz o innych graczach,

            assertEvent(ServerEvent.Type.Commands, new String[]{"chat"}, messages[2]);
            uuids.add(client.getUUID().toString());

        }
    }

    @Test
    public void testOnMoreThanOneClientConnected() throws Exception {
        Lobby.getInstance().maxPlayerCount = 16;

        PlayerMock client = null, lastClient = null;
        ServerMessage[] messages = null;

        for(int i = 0; i < Lobby.getInstance().maxPlayerCount-1; i++) { // Ostatni gracz sprowadziłby zmianę stanu
                                                                            // i wiecej wiadomosci
            if(client != null) lastClient = client;
            client = createClient();
            Lobby.getInstance().onClientConnected(client);
            client.getLastMessages();

            if(lastClient != null) {
                messages = lastClient.getLastMessages();

                assertEquals(1, messages.length);
                assertEvent(ServerEvent.Type.ClientConnect, new String[] {client.getUUID().toString()}, messages[0]);

            }
        }
    }

    @Test
    public void testLastClientConnected() throws Exception {
        Lobby.getInstance().maxPlayerCount = 16;
        ServerMessage[] messages = null;
        ArrayList<PlayerMock> clients = new ArrayList<>();

        for(int i = 0; i < Lobby.getInstance().maxPlayerCount - 1; i++) {
            PlayerMock client = createClient();
            Lobby.getInstance().onClientConnected(client);

            client.getLastMessages();
            clients.add(client);
        }

        //Last client:
        PlayerMock client = createClient();
        Lobby.getInstance().onClientConnected(client);
        messages = client.getLastMessages();
        assertEquals(5, messages.length);
        assertEvent(ServerEvent.Type.ChangeState, new String[] { "Licitation"}, messages[3]);

        for(int i = 0; i < Lobby.getInstance().maxPlayerCount - 1; i++) {
            client = clients.get(i);

            messages = client.getLastMessages();
            assertTrue(Lobby.getInstance().maxPlayerCount - i +1 <= messages.length);
            assertEvent(ServerEvent.Type.ChangeState,
                    new String[] { "Licitation" }, messages[Lobby.getInstance().maxPlayerCount-1-i]);
        }
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