package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.gamestates.Lobby;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;

import java.util.ArrayList;

/**
 * Created by frondeus on 23.01.16.
 */
public class Licitation implements IState {
    private volatile  static Licitation instance;
    public static Licitation getInstance() {
        if(instance == null) {
            synchronized(Licitation.class) {
                if(instance == null) instance = new Licitation();
            }
        }
        return instance;
    }

    public static void resetInstance() {
        synchronized (Licitation.class) {
            instance = null;
        }
    }

    private Deal deal;

    private Licitation() {
        deal = GamePlay.getInstance().getDeal();
    }

    @Override
    public void onStart() {
        /*
        Deck deck = GamePlay.getInstance().getDeck();
        deck.reset();
        deck.shuffle();

        for(IPlayer player: GamePlay.getInstance().getPlayers()) {
            GamePlay.getInstance().broadcast(new ServerEvent(
                    ServerEvent.Type.Bet,
                    new String[] {
                            player.getUUID().toString(),
                            Integer.toString(player.getMoney()),
                            Integer.toString(0)
                    }
                    ));


            try {
                Card first = deck.drawLast();
                Card second = deck.drawLast();
                player.setHand(new Card[] { first, second });
                GamePlay.getInstance().sendHand(player, first, second);
            } catch (Exception e) {
                Logger.exception(e);
            }

        }

*/
    }

    @Override
    public String[] getAvailableCommands() {
        return new String[] {"Foo", "Bar"};
    }

    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {
        /*
        Logger.status("Command: " + message.getCommand());
        Deck deck = GamePlay.getInstance().getDeck();

        switch(message.getCommand()) {
        case "Foo":
            Logger.status("Flop");
            //TODO: Przenieść stąd do stanu Flop
            try {
                Card[] flop = new Card[]{ deck.drawLast() };
                    GamePlay.getInstance().sendSharedCard(flop[0]);
            }
            catch (Exception e) {
                Logger.exception(e);
            }
            break;
            case "Bar":
                Logger.status("Showdown");
                //TODO: Przenieść stąd do stanu Showdown
                for(IPlayer p: GamePlay.getInstance().getPlayers()) {
                    ArrayList<String> args = new ArrayList<>();
                    for(IPlayer other: GamePlay.getInstance().getPlayers()) {
                        if(p!= other) {
                            Card[] hand = other.getHand();
                            GamePlay.getInstance().sendOtherHand(p, other, hand[0], hand[1]);
                        }
                    }
                }
            break;
        }

        deal.nextPlayer();
        */
    }
}
