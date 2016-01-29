package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.server.IPlayer;

public class LicitationNoLimit extends Licitation{
    public LicitationNoLimit(){
    	super();
    }

	/*TODO: check if bet is fair according to licitation type
                check if betValue > bigBlind
                check if player has enough money to bet betValue
                check if betValue+playerBets => biggestBet
    */
	public boolean betIsFair(IPlayer player, int betValue){
		return true;
	}

	@Override
	public int getRequiredBet(IPlayer player) {
		return biggestBet - deal.sumBetAmount(player);
	}

	/*TODO in subclasses: return information about licitation type*/
    public String getLicitationType(){
    	return "NoLimit";
    }
}