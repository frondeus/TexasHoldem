package lubiezurek.texasholdem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import lubiezurek.texasholdem.server.Server;
import lubiezurek.texasholdem.server.ServerOptions;

class Program {

    public static void main(String[] args) {
        try {
            int maxRaiseFixedLimit, fixedLimitBet;

            ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
            int port = 7777;

            if(a.size() > 0) {
                port = Integer.parseInt(a.get(0));
                a.remove(0);
            }
            Server.getInstance(port,new ServerOptions(a.toArray(new String[]{}))).run();
        }
        catch(Exception e) {
            Logger.exception(e);
        }
    }

}
