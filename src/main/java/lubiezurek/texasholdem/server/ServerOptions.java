package lubiezurek.texasholdem.server;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.server.states.Licitation;

/**
 * Created by frondeus on 26.01.16.
 */
public class ServerOptions {
    private int maxPlayerCount = 4;
    private int startMoney = 199;
    private int smallBlind = 5;
    private int bigBlind = 10;

    private LicitationType licitationType = LicitationType.NoLimit;
    private int maxRaiseFixedLimit = 5;
    private int fixedLimitBet = 10;

    public ServerOptions() {}
    public ServerOptions(String[] args) {
        if(args.length > 0) maxPlayerCount = Integer.parseInt(args[0]);
        if(args.length > 1) startMoney = Integer.parseInt(args[1]);
        if(args.length > 2) smallBlind = Integer.parseInt(args[2]);
        if(args.length > 3) bigBlind = Integer.parseInt(args[3]);

        if(args.length > 4) licitationType = LicitationType.valueOf(args[4]);
        if(args.length > 5) maxRaiseFixedLimit = Integer.parseInt(args[5]);
        if(args.length > 6) fixedLimitBet = Integer.parseInt(args[6]);

        Logger.status("Max player count: "+ maxPlayerCount);
        Logger.status("Start money: "+ startMoney);
        Logger.status("Small blind: "+smallBlind);
        Logger.status("Big blind: "+bigBlind);
        Logger.status("Licitation type: "+licitationType.name());
        if(licitationType == LicitationType.FixedLimit) {
            Logger.status("Max raise limit: "+maxRaiseFixedLimit);
            Logger.status("Bet amount: "+fixedLimitBet);
        }
    }


    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public int getStartMoney() {
        return startMoney;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public LicitationType getLicitationType() {
        return licitationType;
    }

    public int getMaxRaiseFixedLimit() {
        return maxRaiseFixedLimit;
    }

    public int getFixedLimitBet() {
        return fixedLimitBet;
    }

    public enum LicitationType {
        NoLimit,
        PotLimit,
        FixedLimit
    }

}
