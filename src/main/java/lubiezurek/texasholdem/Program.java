package lubiezurek.texasholdem;

import java.io.IOException;

import lubiezurek.texasholdem.server.Server;

class Program {
    private static final String DEFAULT_PORT = "7777";
    private static final String DEFAULT_LOCALHOST = "127.0.0.1";

    public static void main(String[] args) {
        try {


            if(args.length == 0) {
                String[] default_args = new String[1];
                default_args[0] = DEFAULT_PORT;
                args = default_args;

            }

            if (args.length != 1) {
                Logger.error("Invalid number of arguments");
                return;
            }
            Server.getInstance(Integer.parseInt(args[0])).run();
        }
        catch(Exception e) {
            Logger.exception(e);
        }
    }

}
