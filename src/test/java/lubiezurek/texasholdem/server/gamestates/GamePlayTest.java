package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.TestHelper;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.states.Licitation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by frondeus on 26.01.16.
 */
public class GamePlayTest extends TestHelper {
    ArrayList<IPlayer> players;
    Deal deal = spy(new Deal());

    @Before
    public void setUp() throws Exception {
        setUpLobby();
        addRestPlayers();
        addPlayer();
        GamePlay.resetInstance();
        Logger.status("Set Deal: " + deal);
        GamePlay.getInstance().setDeal(deal);
        Lobby.getInstance().changeState();
        players = GamePlay.getInstance().getPlayers();
    }

    /*
        >> Gameplay się zaczyna. Musimy:
            * Ustalić kolejkę
            * Wylosowac dealer button.
            * Zacząć 1 rozdanie.
                * Mała ciemna na lewo od dealera
                * Duża ciemna na lewo od małej
                * Talia tasowana
                * Rozdać karty do ręki.
                * > Licytacja na lewo od dużej ciemnej
                * > Flop (3 karty wspolne)
                * > Licytacja od malej ciemnej
                * > Turn (4 karta)
                * > Licytacja od malej ciemnej
                * > River (5 karta)
                * > Licytacja od malej ciemnej
                * > Showdown
            * następny dealer
            * Zacząć N rozdanie
            * Ustawic na >> AfterGame
     */

    @Test
    public void onEnterShouldCreateQueue() {
        for (IPlayer player: players) {
            assertNotNull(player.getNextPlayer());
        } // Upewniam się że każdy ma następce

        IPlayer firstPlayer = players.get(0);
        IPlayer nextPlayer = firstPlayer.getNextPlayer();
        int moves = 1;
        while(nextPlayer != firstPlayer) {
            nextPlayer = nextPlayer.getNextPlayer();
            moves++;
        }
        assertEquals(firstPlayer, nextPlayer); // Upewniam się że graf który powstał jest cykliczny
        assertEquals(players.size(), moves); // I składa się dokładnie z tylu elementów
    }

    @Test
    public void onEnterShouldRandomDealer() {
        assertNotNull(deal);
        IPlayer firstPlayer = players.get(0);
        assertEquals(firstPlayer, deal.getDealer());
    }

    @Test
    public void onEnterShouldSetupLicitationState() {
        assertNotNull(GamePlay.getInstance().getLicitationState());
        switch(Options().getLicitationType()) {
        default:
            //TODO Różne typy licytacji
            assertEquals(Licitation.getInstance(), GamePlay.getInstance().getLicitationState());
            break;
        }
    }

    @Test
    public void onEnterShouldSetupDeal() {
        verify(deal, times(1)).setUp();
        assertEquals(GamePlay.getInstance().getLicitationState(), deal.getState());
    }

}