package lubiezurek.texasholdem.server.deal;

import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.gamestates.Lobby;
import lubiezurek.texasholdem.server.model.Deck;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by frondeus on 27.01.16.
 */
public class DealTest extends TestHelper {
    private ArrayList<IPlayer> players;
    private Deal deal;
    private Deck deck;


    @Before
    public void setUp() throws Exception {
        setUpLobby();   // Ustawic lobby
        addRestPlayers();   // Dodać n-1 graczy
        addPlayer();    // Dodac ostatniego gracza
        GamePlay.resetInstance();   // Ustawic Gameplay
        players = Lobby.getInstance().getPlayers();
        deal = spy(new Deal());
        deck = spy(new Deck());
        GamePlay.getInstance().setDeal(deal);
        deal.setDeck(deck);
        Lobby.getInstance().changeState();  // Zmienic recznie stan na GamePlay
        //Gameplay odpali deal.Enter();
    }

    //TODO: Repair this
    /*@Test
    public void startShouldSetSmallAndBigBlindPlayers() {
        PlayerMock dealer = (PlayerMock)GamePlay.getInstance().getDealer();
        PlayerMock smallBlindPlayer = (PlayerMock)deal.getSmallBlindPlayer();
        PlayerMock bigBlindPlayer = (PlayerMock)deal.getBigBlindPlayer();

        assertNotNull(dealer);
        assertEquals(smallBlindPlayer, dealer.getNextPlayer());
        assertEquals(bigBlindPlayer, dealer.getNextPlayer().getNextPlayer());
    }*/

    //TODO: Repair this
    /*@Test
    public void startShouldAddSmallBlindBet() {
        PlayerMock smallBlindPlayer = (PlayerMock)deal.getSmallBlindPlayer();

        verify(deal, times(1)).addBet(smallBlindPlayer, Options().getSmallBlind());

    }*/

    //TODO: Repair this
    /*@Test
    public void startShouldAddBigBlindBet() {
        PlayerMock bigBlindPlayer = (PlayerMock)deal.getBigBlindPlayer();

        verify(deal, times(1)).addBet(bigBlindPlayer, Options().getBigBlind());
    }*/


    @Test
    public void startShouldStartLicitation() {
        verify(deal, times(1)).setState(GamePlay.getInstance().getLicitationState());
    }

    private Bet lastBet() {
        return deal.getBets().get(deal.getBets().size()-1);
    }

    @Test
    public void addBetShouldAddBetToList() {
        PlayerMock player = (PlayerMock) players.get(0);

        int oldSize = deal.getBets().size();
        deal.addBet(player, 10);
        int newSize = deal.getBets().size();
        assertEquals(1, newSize - oldSize);
        assertEquals(player, lastBet().getPlayer());
        assertEquals(10, lastBet().getAmount());
    }

    //TODO: Repair this
    /*@Test
    public void addBetWhenAnyPlayerHasLessMoneyThanSmallBlindShouldDoAllIn(){
        PlayerMock player = (PlayerMock) deal.getSmallBlindPlayer();

        while(deal.availableMoney(player) >= Options().getSmallBlind())
            deal.addBet(player, (int)(Options().getSmallBlind() * 0.5));

        //Ok ma mniej niz smallBlind. I musi teraz dac jeszcze raz.

        assertTrue(deal.availableMoney(player) > 0);
        deal.addBet(player,Options().getSmallBlind());
        assertTrue(deal.availableMoney(player) == 0);
    }*/

    //TODO: Repair this
    /*@Test
    public void addBetWhenRegularPlayerHasLessMoneyThanBigBlindButMoreThanSmallBlindShouldDoAllIn() {
        PlayerMock player = (PlayerMock) players.get(0);

        while(deal.availableMoney(player) >= Options().getBigBlind())
            deal.addBet(player, (int)((Options().getBigBlind() - Options().getSmallBlind()) * 0.5));

        //Ok teraz ma mniej niż bigBlind ale wiecej niz small blind.

        assertTrue(deal.availableMoney(player) > Options().getSmallBlind());
        deal.addBet(player, Options().getBigBlind());
        assertTrue(deal.availableMoney(player) == 0);
    }*/

    @Test
    public void addBetShouldBroadcastBetEvent() {
        PlayerMock player = (PlayerMock) players.get(0);
        PlayerMock otherPlayer = (PlayerMock) player.getNextPlayer();
        player.getLastMessages();
        otherPlayer.getLastMessages();

        deal.addBet(player, 10);

        ServerMessage[] messages = otherPlayer.getLastMessages();
        assertEquals(1, messages.length);
        assertEvent(ServerEvent.Type.Bet, new String[] {
                player.getUUID().toString(),
                Integer.toString(player.getMoney()),
                Integer.toString(deal.sumBetAmount(player))
        }, messages[0]);

    }

    @Test
    public void setStateShouldSetNewState() {
        IState state = mock(IState.class);
        deal.setState(state);
        assertEquals(state, deal.getState());
    }

    @Test
    public void setStateShouldBroadcastChangeStateEvent() {
        PlayerMock player = (PlayerMock) players.get(0);
        IState state = mock(IState.class);
        player.getLastMessages();

        deal.setState(state);
        ServerMessage[] messages = player.getLastMessages();

        assertEquals(1, messages.length);
        assertEvent(ServerEvent.Type.ChangeState, new String[] {
                state.getClass().getSimpleName()
        }, messages[0]);
    }

    @Test
    public void setStateShouldCallStateOnStart() {
        IState state = mock(IState.class);

        deal.setState(state);
        verify(state, times(1)).onStart(deal);
    }

    @Test
    public void addBetShouldTakeAwayMoneyFromPlayer(){
        PlayerMock player = (PlayerMock) players.get(0);
        int money_before = player.getMoney();

        deal.addBet(player, 10);

        assertEquals(player.getMoney(), money_before - 10);

    }

}