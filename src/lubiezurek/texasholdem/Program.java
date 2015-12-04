package lubiezurek.texasholdem;

import java.io.IOException;

import lubiezurek.texasholdem.client.Client;
import lubiezurek.texasholdem.server.Server;

public class Program {
	static final String DEFAULT_PORT = "7777";

	public static void main(String[] args) {
		try {
			if(args.length < 1) {
				Logger.error("Invalid number of arguments");
				return;
			}
			
			String typ = args[0];
			if(typ.equals("-s") || typ.equals("--server")) {
				if(args.length != 2) {
					args = new String[2];
					args[0] = "-s";
					args[1] = DEFAULT_PORT; 
				}
				Server server = new Server(args);
				server.run();
			}
			else if(typ.equals("-c") || typ.equals("--client")) {
				if(args.length != 3) {
					Logger.error("Invalid number of arguments");
					return;
				}
				Client client = new Client(args);
				client.run();
			}
			else {
				Logger.error("Invalid argument: " + typ);
			}
		}
		catch(IOException e) {
			Logger.exception(e);
		}
	}

}
