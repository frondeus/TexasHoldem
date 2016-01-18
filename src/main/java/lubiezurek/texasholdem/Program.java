package lubiezurek.texasholdem;

import java.io.IOException;

import lubiezurek.texasholdem.server.Server;

class Program {
    private static final String DEFAULT_PORT = "7777";
    private static final String DEFAULT_LOCALHOST = "127.0.0.1";

    public static void main(String[] args) {
        try {
            if(args.length == 0) {
                Logger.error("Invalid number of arguments");
                return;
            }

            String typ = args[0];

            if(args.length == 1) {
                switch (typ) {
                    case "-s":
                    case "--server": {
                                         String[] default_args = new String[2];
                                         default_args[0] = args[0];
                                         default_args[1] = DEFAULT_PORT;
                                         args = default_args;
                                         break;
                    }
                }

            }

            switch (typ) {
                case "-s":
                case "--server":
                    if (args.length != 2) {
                        Logger.error("Invalid number of arguments");
                        return;
                    }
                    Server.getInstance(Integer.parseInt(args[1])).run();
                    break;
                default:
                    Logger.error("Invalid argument: " + typ);
                    break;
            }
        }
        catch(Exception e) {
            Logger.exception(e);
        }
    }

}
