package lubiezurek.texasholdem.server.gamestates;

import lubiezurek.texasholdem.StateMock;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.*;
import lubiezurek.texasholdem.server.deal.Bet;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.states.Licitation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by frondeus on 26.01.16.
 */
public class GamePlayTest extends TestHelper {
    ArrayList<IPlayer> players;
    Deal deal = spy(new Deal());
    IState mockState = spy(new StateMock());

    @Before
    public void setUp() throws Exception {
        //Za każdym razem gdy testuje jakas funkcjonalnosc chce zrobic pare rzeczy:
        setUpLobby();   // Ustawic lobby
        addRestPlayers();   // Dodać n-1 graczy
        addPlayer();    // Dodac ostatniego gracza
        GamePlay.resetInstance();   // Ustawic Gameplay
        GamePlay.getInstance().setDeal(deal); // Ustawic szpiega na deal (aby wiedziec czy wywolywane byly funkcje itd)
        Lobby.getInstance().changeState();  // Zmienic recznie stan na GamePlay
        players = GamePlay.getInstance().getPlayers();  // Pobrac liste graczy
    }

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
        assertEquals(firstPlayer, GamePlay.getInstance().getDealer());
    }

    @Test
    public void onEnterShouldSetupMoney() {
        for(IPlayer player : players) {
            assertEquals(Options().getStartMoney(), player.getMoney());
        }
    }

    @Test
    public void onEnterShouldBroadcastMoney() {
        PlayerMock player = (PlayerMock) players.get(0);
        player.getLastMessages();

        GamePlay.getInstance().setupMoney();

        ServerMessage[] messages = player.getLastMessages();
        assertEquals(players.size(),  messages.length);
        for(int i = 0; i < players.size(); i++) {
            IPlayer otherPlayer = players.get(i);
            assertEvent(ServerEvent.Type.Bet, new String[] {
                    otherPlayer.getUUID().toString(),
                    Integer.toString(otherPlayer.getMoney()),
                    "0"
            },messages[i]);
        }
    }

    @Test
    public void onEnterShouldSetupDeal() {
        verify(deal, times(1)).start(GamePlay.getInstance().getDealer());
    }

    @Test
    @Ignore
    public void changeStateShould() {
        /*

            Dodać testy i warunek sprawdzający czy gra już się zakończyła.
            changeState jest wywolywane po kazdym dolaczeniu sie gracza i wiadomosci gracza.
            Wiec wystarczy warunek if(warunek czy juz przelaczyc na AfterGame) {
                przelacz sie.
            }

         */
    }

    @Test
    public void nextDealerShouldSetNextDealer() {
        IPlayer dealer = GamePlay.getInstance().getDealer();
        GamePlay.getInstance().nextDealer();
        IPlayer next = GamePlay.getInstance().getDealer();

        assertEquals(dealer.getNextPlayer(), next);
    }

    @Test
    public void onClientConnectedShouldSendResponse() {
        PlayerMock playerMock = new PlayerMock();
        GamePlay.getInstance().onClientConnected(playerMock);
        ServerMessage[] messages = playerMock.getLastMessages(); // PlayerMock.getLastMessages() returnuje liste
        //Wiadomosci jakie doszlyby do gracza, po czym usuwa je z wewnętrznej listy. Czyli drugie użycie
        //Pod rząd zwróci już pustą listę. Używaj tego feature przed testowaną akcją aby mieć pewność że dojdzie tylko
        //Ten message na którym ci zależy
        assertEquals(1, messages.length);
        assertResponse(ServerResponse.Status.Failure, "Game is in progress", messages[0]);
        assertTrue(playerMock.isDisconnected());
    }

    @Test
    public void onClientMessageOnUnknownMessageShouldSendResponse(){
        deal.setState(mockState);

        PlayerMock player = (PlayerMock) players.get(0);
        when(mockState.isPlayerTurn(player)).thenReturn(true);

        player.getLastMessages();
        ClientMessage message = new ClientMessage("Unknown1234",new String[]{});
        GamePlay.getInstance().onClientMessage(player,message);
        ServerMessage[] messages = player.getLastMessages();
        assertEquals(1, messages.length);
        assertResponse(ServerResponse.Status.Failure, "Unknown command", messages[0]);

    }

    @Test
    public void onClientMessageWhenNotPlayersTurnShouldResponse() {
        deal.setState(mockState);

        PlayerMock player = (PlayerMock) players.get(0);
        when(mockState.isPlayerTurn(player)).thenReturn(false);
        when(mockState.getAvailableCommands(player)).thenReturn(new String[] { "Foo" });

        player.getLastMessages();
        ClientMessage message = new ClientMessage("Foo", new String[] {});
        GamePlay.getInstance().onClientMessage(player, message);
        ServerMessage[] messages = player.getLastMessages();
        assertEquals(1, messages.length);
        assertResponse(ServerResponse.Status.Failure, "It's not your turn", messages[0]);
    }

    @Test
    public void onClientMessageShouldCallDealState() {
        deal.setState(mockState);

        PlayerMock player = (PlayerMock) players.get(0);
        when(mockState.isPlayerTurn(player)).thenReturn(true);
        when(mockState.getAvailableCommands(player)).thenReturn(new String[] { "Foo" });

        player.getLastMessages();
        ClientMessage message = new ClientMessage("Foo", new String[] {});
        GamePlay.getInstance().onClientMessage(player, message);
        verify(mockState, times(1)).onPlayerMessage(player, message);
    }

    @Test
    public void onClientDisconnectedShouldRemovePlayerFromList() {
        PlayerMock player = (PlayerMock) players.get(0);
        player.getLastMessages();

        assertTrue(players.contains(player));
        GamePlay.getInstance().onClientDisconnected(player);
        players = GamePlay.getInstance().getPlayers();
        assertFalse(players.contains(player));
    }

    @Test
    public void onClientDisconnectedShouldBroadcastEvent() {
        PlayerMock player = (PlayerMock) players.get(0);
        PlayerMock second = (PlayerMock) players.get(1);
        second.getLastMessages();

        GamePlay.getInstance().onClientDisconnected(player);
        ServerMessage[] messages = second.getLastMessages();
        assertEquals(1, messages.length);
        assertEvent(ServerEvent.Type.ClientDisconnect, new String[] {
                player.getUUID().toString()
        },messages[0]);

    }

    @Test
    public void onClientDisconnectedShouldRepairQueue() {
        GamePlay.getInstance().onClientDisconnected(players.get(0));
        players = GamePlay.getInstance().getPlayers();

        IPlayer firstPlayer = players.get(0);
        IPlayer nextPlayer = firstPlayer.getNextPlayer();
        int moves = 1;
        while(nextPlayer != firstPlayer) {
            nextPlayer = nextPlayer.getNextPlayer();
            moves++;
        }
        assertEquals(firstPlayer, nextPlayer);
        assertEquals(players.size(), moves);
    }

    @Test
    @Ignore
    public void onClientDisconnectedShouldReplaceWithBot() {

    }


}