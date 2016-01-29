package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.gamestates.Lobby;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by frondeus on 26.01.16.
 */
public class TestHelper {
    protected ArrayList<String> uuids;

    protected void assertResponse(ServerResponse.Status expectedStatus,
                                  String expectedMessage,
                                  ServerMessage message) {
        assertEquals(ServerResponse.class, message.getClass());
        ServerResponse response = (ServerResponse) message;
        assertEquals(expectedStatus, response.getStatus());
        assertEquals(expectedMessage, response.getMessage());
    }

    protected  void assertEvent(ServerEvent.Type expectedType,
                                ArrayList<String> expectedArgs,
                                ServerMessage message) {
        assertEvent(expectedType, expectedArgs.toArray(new String[]{}), message);
    }
    protected  void assertEvent(ServerEvent.Type expectedType,
                                String[] expectedArgs,
                                ServerMessage message) {
        assertEquals(ServerEvent.class, message.getClass());
        ServerEvent event = (ServerEvent)message;

        assertEquals(expectedType, event.getEventType());
        assertEquals(expectedArgs.length, event.getArguments().length);
        for(int i = 0; i < expectedArgs.length; i++)
            assertEquals(expectedArgs[i], event.getArguments()[i]);
    }

    protected ServerOptions Options() {
        return Server.getInstance().Options;
    }


    protected PlayerMock addPlayer() {
        PlayerMock player = new PlayerMock();
        Lobby.getInstance().onClientConnected(player);
        uuids.add(player.getUUID().toString());
        return player;
    }

    protected PlayerMock addRestPlayers() {
        PlayerMock lastPlayer = null, player = null;
        for(int i = 0; i < Options().getMaxPlayerCount() -1; i++) {
            player = addPlayer();
        }
        if(player != null) lastPlayer = player;
        return lastPlayer;
    }

    protected void setUpLobby() {
        uuids = new ArrayList<>();
        Lobby.resetInstance();
        Server.getInstance().setState(Lobby.getInstance());
    }

}
