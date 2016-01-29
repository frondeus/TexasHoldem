package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.model.Deck;
import lubiezurek.texasholdem.server.model.card.Card;

/**
 * Created by mrmino on 29.01.16.
 */
public class Shuffle implements IState{
    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {
    }

    @Override
    public void onStart(Deal deal) {
        Deck deck = deal.getDeck();
        if(!deal.playersHaveCards()){
            for (IPlayer p : GamePlay.getInstance().getPlayers()) {
                Card c1 = deck.drawLast();
                Card c2 = deck.drawLast();
                Card[] hand = new Card[2];
                hand[0] = c1;
                hand[1] = c2;

                p.setHand(hand);

                p.sendMessage(new ServerEvent(ServerEvent.Type.Hand,
                        new String[] {c1.toString(), c2.toString()}
                        ));
            }

            deal.setPlayersHaveCards(true);
            return;
        }else if(deal.getFlop() == null){
            Card c1 = deck.drawLast();
            Card c2 = deck.drawLast();
            Card c3 = deck.drawLast();
            Card[] flop = new Card[3];
            flop[0] = c1;
            flop[1] = c2;
            flop[2] = c3;

            deal.setFlop(flop);
            GamePlay.getInstance().broadcast(new ServerEvent(
                    ServerEvent.Type.SharedCard,
                    new String[] {"flop", c1.toString(), c2.toString(), c3.toString()}
            ));

            deal.setState(GamePlay.getInstance().getLicitationState());

        }else if(deal.getTurn() == null){
            Card turn = deck.drawLast();

            deal.setTurn(turn);
            GamePlay.getInstance().broadcast(new ServerEvent(
                    ServerEvent.Type.SharedCard,
                    new String[] {"turn", turn.toString()}
            ));
            deal.setState(GamePlay.getInstance().getLicitationState());

        }else if(deal.getRiver() == null){
            Card river = deck.drawLast();

            deal.setRiver(river);
            GamePlay.getInstance().broadcast(new ServerEvent(
                    ServerEvent.Type.SharedCard,
                    new String[] {"river", river.toString()}
            ));

            deal.setState(GamePlay.getInstance().getLicitationState());

        }else{
            //TODO: uncomment this when showdown state class is done
            //deal.setState(new Showdown);
        }
    }

    @Override
    public String[] getAvailableCommands(IPlayer forPlayer) {
        return new String[] {};
    }

    @Override
    public boolean isPlayerTurn(IPlayer player) {
        return false;
    }
}
