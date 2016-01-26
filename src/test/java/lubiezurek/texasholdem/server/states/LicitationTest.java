package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.server.PlayerMock;
import lubiezurek.texasholdem.server.Server;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.gamestates.Lobby;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frondeus on 26.01.16.
 */
public class LicitationTest {

    @Before
    public void setUp() throws Exception {
        //GamePlay.resetInstance();
        //Licitation.resetInstance();
        //Lobby.resetInstance();

        //for(int i = 0; i < Server.getInstance().Options.getMaxPlayerCount(); i++)
            //Lobby.getInstance().onClientConnected(new PlayerMock());

        //Server.getInstance().setState(GamePlay.getInstance());
        //Licitation is ready
        //assertEquals(Licitation.getInstance(), GamePlay.getInstance().deal.getState());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testOnStartShouldShuffleDeck() {

    }
}