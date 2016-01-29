package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.ServerEvent;
import lubiezurek.texasholdem.server.ServerResponse;
import lubiezurek.texasholdem.server.deal.Deal;
import lubiezurek.texasholdem.server.gamestates.GamePlay;
import lubiezurek.texasholdem.server.model.card.Card;
import lubiezurek.texasholdem.server.model.card.CardValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by frondeus on 28.01.16.
 */
public class Showdown implements IState {
    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {
        player.sendMessage(new ServerResponse(ServerResponse.Status.Failure,
                "Showdown time. No action is needed."));
    }

    public int addToSum[] = { 0,   59, 123,    189,    231,    291,    350,    418,    474 };
    private int maxSums[] = { 59,  64, 66,     42,     60,     59,     68,     56,     60};

    private boolean isInArray(int[] array, int element) {
        for (int i = 0; i < array.length; i++)
            if(array[i] == element) return true;
        return false;
    }

    private int sumHighest(int[] cards, int n) {
        int sum = 0;
        for(int i = 0; i < n && i < cards.length; i++) {
            if(cards[i] > 0) sum += valueOf(i) * cards[i];
            else n++;
        }
        return sum;
    }

    private int sumHighestExcept(int exception, int[] cards, int n) {
        return sumHighestExcept(new int[]{exception}, cards, n);
    }

    private int sumHighestExcept(int[] exception, int[] cards, int n) {
        int sum = 0;
        for(int i = 0; i < n && i < cards.length; i++) {
            if(cards[i] > 0 && !isInArray(exception, i)) sum += valueOf(i) * cards[i];
            else n++;
        }
        return sum;
    }

    private int valueOf(int i) {
        return 14 - i;
    }

    public int evaluatePlayer(IPlayer player, Card[] table) {
        //              A K Q J 10 9 8 7 6 5 4 3 2
        int cards[] = { 0,0,0,0,0 ,0,0,0,0,0,0,0,0};

        ArrayList<Card> suits[] = new ArrayList[4];//{ 0,0,0,0 };
        for(int i = 0; i < 4; i++) suits[i] = new ArrayList<>();

        for(Card card: player.getHand()) {
            cards[cards.length - 1 - (card.getCardValue().getValue() - 2)]++;
            suits[card.getSuit().getValue()].add(card);
        }
        for(Card card: table){
            cards[cards.length - 1 - (card.getCardValue().getValue() - 2)]++;
            suits[card.getSuit().getValue()].add(card);
        }

        for(ArrayList<Card> suit: suits) {
            Collections.sort(suit, (c1, c2) -> {
                int v1 = c1.getCardValue().getValue();
                int v2 = c2.getCardValue().getValue();
                return Integer.compare(v2,v1);
            });
        }

        int value = isPoker(suits,cards); //1
        if(value == 0) value = isFourOfAKind(cards); //2
        else value += maxSums[7];
        if(value == 0) value = isFullHouse(cards); //3
        else value += maxSums[6];
        if(value == 0) value = isFlush(suits, cards); //4
        else value += maxSums[5];
        if(value == 0) value = isStraight(cards); //5
        else value += maxSums[4];
        if(value == 0) value = isThreeOfAKind(cards); //6
        else value += maxSums[3];
        if(value == 0) value = isTwoPair(cards); //7
        else value += maxSums[2];
        if(value == 0) value = isOnePair(cards); //8
        else value += maxSums[1];
        if(value == 0) value = sumHighest(cards, 5); //9
        else value += maxSums[0];
         return value;
    }


    private int isOnePair(int[] cards) {
        int sum = 0;
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == 2) {
                sum += valueOf(i)*2;
                sum += sumHighestExcept(i, cards, 3);
                break;
            }
        }

        return sum;
    }

    private int isTwoPair(int[] cards) {
        int sum = 0;
        int j = -1;
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == 2) {
                if(j == -1) j = i;
                else if(j != i) {
                    sum += valueOf(i)*2;
                    sum += valueOf(j)*2;
                    sum += sumHighestExcept(new int[]{i,j}, cards, 1);
                    break;
                }
            }
        }
        return sum;
    }

    private int isThreeOfAKind(int[] cards) {
        int sum = 0;
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == 3) {
                sum += valueOf(i) * 3;
                break;
            }
        }
        return sum;
    }

    private int isStraight(int[] cards) {
        int n = 0;
        int sum = 0;
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == 1) {
                n++;
                sum += valueOf(i);
            }
            else if(n < 5){
                n = 0;
                sum = 0;
            }
            else break;
        }
        if(n < 5) sum = 0;

        return sum;
    }

    private int isFlush(ArrayList<Card>[] suits, int[] cards) {
        int sum = 0;
        for(int i = 0; i < suits.length; i++) {
            if(suits[i].size() >= 5) {
                for(int j = 0; j < 5; j++) {
                    Card card = suits[i].get(j);
                    sum += card.getCardValue().getValue();
                }
                break;
            }
        }
        return 0;
    }

    private int isFullHouse(int[] cards) {
        int bestPair = -1;
        int bestThree = -1;
        for(int i = 0; i < cards.length; i++)
            if(cards[i] == 3) {
                bestThree = i;
                break;
            }

        for(int i = 0; i < cards.length; i++)
            if(cards[i] == 2) {
                bestPair = i;
                break;
            }

        if(bestPair > -1 && bestThree > -1)
            return 3 * valueOf(bestThree);
        return 0;
    }

    private int isFourOfAKind(int[] cards) {
        int sum = 0;
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == 4) {
                sum = 4 * valueOf(i);
                break;
            }
        }
        return sum;
    }

    private int isPoker(ArrayList<Card>[] suits, int[] cards) {
        int sum = 0;
        Card previous = null;
        for(int i = 0; i < suits.length; i++) {
            if(suits[i].size() >= 5) {
                Card card = null;
                for(int j = 0; j < 5; j++) {
                    if(card != null) previous = card;

                    card = suits[i].get(j);
                    if((previous.getCardValue().getValue() - card.getCardValue().getValue() != 1))
                        return 0;
                    sum += card.getCardValue().getValue();
                }
            }
        }
        return sum;
    }

    private void sendOtherHand(IPlayer player, IPlayer other) {
        player.sendMessage(new ServerEvent(
                ServerEvent.Type.OtherHand,
                new String[] {
                        other.getUUID().toString(),
                        other.getHand()[0].toString(),
                        other.getHand()[1].toString()
                }));
    }
    @Override
    public void onStart(Deal deal) {
        Card[] table = new Card[] {
                deal.getFlop()[0],
                deal.getFlop()[1],
                deal.getFlop()[2],
                deal.getTurn(),
                deal.getRiver()
        };
        int maxValue = 0;
        ArrayList<IPlayer> winners = new ArrayList<>();

        for(IPlayer player: GamePlay.getInstance().getPlayers()) {
            int value = evaluatePlayer(player,table );
            if(value > maxValue) {
                winners.clear();
                winners.add(player);
                maxValue = value;
            }
            else if(value == maxValue)
                winners.add(player);
        }

        Logger.status("Winners: ");
        for(IPlayer player: winners)
            Logger.status(player.getUUID().toString());

        for(IPlayer player: GamePlay.getInstance().getPlayers()) {
            for(IPlayer other: GamePlay.getInstance().getPlayers()) {
                if(player != other) {
                    sendOtherHand(player, other);
                }
            }
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
