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
public class LobbyTest extends TestHelper {


    @Before
    public void setUp() {
        setUpLobby();
    }

    @Test (expected = IllegalArgumentException.class)
    public void onClientConnectedOnNullShouldThrowException() throws Exception {
        Lobby.getInstance().onClientConnected(null);
    }


    @Test
    public void onClientConnectedShouldAddPlayer() throws Exception {
        ArrayList<IPlayer> players = Lobby.getInstance().getPlayers();

        PlayerMock player = new PlayerMock();
        assertFalse(players.contains(player));
        Lobby.getInstance().onClientConnected(player);
        assertTrue(players.contains(player));
        assertFalse(player.isDisconnected());
    }

    @Test
    public void onClientConnectedShouldSendConnectedEvent() {
        addRestPlayers();
        PlayerMock playerMock = addPlayer();
        ServerMessage[] messages = playerMock.getLastMessages();
        assertTrue(1 <= messages.length);
        uuids.remove(uuids.size()-1);
        uuids.add(0, playerMock.getUUID().toString());
        assertEvent(ServerEvent.Type.Connected, uuids, messages[0]);
    }

    @Test
    public void onClientConnectedShouldBroadcastClientConnectedEvent() {
        PlayerMock lastPlayer = addRestPlayers();
        PlayerMock playerMock = addPlayer();
        ServerMessage[] messages = lastPlayer.getLastMessages();
        assertTrue(2 <= messages.length);
        assertEvent(ServerEvent.Type.ClientConnect, new String[]{
                playerMock.getUUID().toString()
        },messages[1]);
    }

    @Test
    public void onClientConnectedWhenFullShouldSendResponse() {
        addRestPlayers();
        addPlayer();
        PlayerMock playerMock = addPlayer();
        ServerMessage[] messages = playerMock.getLastMessages();
        assertEquals(1, messages.length);
        assertResponse(ServerResponse.Status.Failure, "Full server", messages[0]);
        assertTrue(playerMock.isDisconnected());
    }

    @Test
    public void onLastClientConnectedShouldChangeState() {
        addRestPlayers();
        addPlayer();
        Lobby.getInstance().changeState();
        assertEquals(GamePlay.getInstance(), Server.getInstance().getState());
        assertEquals(Lobby.getInstance().getPlayers(), GamePlay.getInstance().getPlayers());
    }

    @Test (expected = IllegalArgumentException.class)
    public void onClientMessageNullPlayerShouldThrowException() {
        Lobby.getInstance().onClientMessage(null, new ClientMessage("chat", new String[]{"hello"}));
    }

    @Test (expected = IllegalArgumentException.class)
    public void onClientMessageNullMessageShouldThrowException() {
        PlayerMock playerMock = addPlayer();
        Lobby.getInstance().onClientMessage(playerMock, null);
    }

    @Test
    public void onClientMessagePlayerNotInListShouldSendResponse() {
        PlayerMock playerMock = new PlayerMock();
        Lobby.getInstance().onClientMessage(playerMock, new ClientMessage("chat",
                new String[]{"hello"}));
        ServerMessage[] messages = playerMock.getLastMessages();
        assertEquals(1, messages.length);
        assertResponse(ServerResponse.Status.Failure, "Not connected", messages[0]);
    }

    @Test
    public void onClientMessageUnknownCommandShouldSendResponse() {
        PlayerMock playerMock = addPlayer();
        playerMock.getLastMessages();
        Lobby.getInstance().onClientMessage(playerMock, new ClientMessage("unknown",
                new String[]{"hello"}));
        ServerMessage[] messages = playerMock.getLastMessages();
        assertEquals(1, messages.length);
        assertResponse(ServerResponse.Status.Failure, "Invalid command", messages[0]);
    }

    @Test
    public void onClientChatShouldBroadcastChatEvent() {
        PlayerMock lastPlayer = addRestPlayers();
        PlayerMock playerMock = addPlayer();
        lastPlayer.getLastMessages();
        playerMock.getLastMessages();

        Lobby.getInstance().onClientMessage(playerMock, new ClientMessage("chat",
                new String[]{"hello"}));

        String[] args = new String[] {
            playerMock.getUUID().toString(),
                "hello"
        };
        ServerMessage[] messages = lastPlayer.getLastMessages();
        assertEquals(1, messages.length);
        assertEvent(ServerEvent.Type.Chat, args, messages[0]);

        messages = playerMock.getLastMessages();
        assertEquals(1, messages.length);
        assertEvent(ServerEvent.Type.Chat, args, messages[0]);

    }

    @Test (expected = IllegalArgumentException.class)
    public void onClientDisconnectedNullPlayerShouldThrowException() {
        Lobby.getInstance().onClientDisconnected(null);
    }

    @Test
    public void onClientDisconnectedShouldRemovePlayer() {
        ArrayList<IPlayer> players = Lobby.getInstance().getPlayers();

        PlayerMock playerMock = addPlayer();
        assertTrue(players.contains(playerMock));
        Lobby.getInstance().onClientDisconnected(playerMock);
        assertFalse(players.contains(playerMock));
    }

    @Test
    public void onClientDisconnectedShouldBroadcastDisconnectedEvent() {
        PlayerMock lastPlayer = addRestPlayers();
        PlayerMock playerMock = addPlayer();
        lastPlayer.getLastMessages();
        playerMock.getLastMessages();
        Lobby.getInstance().onClientDisconnected(playerMock);
        ServerMessage[] messages = lastPlayer.getLastMessages();
        assertEquals(1, messages.length);
        assertEvent(ServerEvent.Type.ClientDisconnect, new String[] {
                playerMock.getUUID().toString()
        },messages[0]);

    }
}
