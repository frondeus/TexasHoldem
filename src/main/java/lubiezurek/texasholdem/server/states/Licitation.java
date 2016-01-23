package lubiezurek.texasholdem.server.states;

import lubiezurek.texasholdem.client.ClientMessage;
import lubiezurek.texasholdem.server.IPlayer;
import lubiezurek.texasholdem.server.IState;
import lubiezurek.texasholdem.server.gamestates.Lobby;

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

    private Licitation() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPlayerMessage(IPlayer player, ClientMessage message) {

    }
}
