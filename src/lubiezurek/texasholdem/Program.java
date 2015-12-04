package lubiezurek.texasholdem;

import java.io.IOException;

import lubiezurek.texasholdem.client.Client;
import lubiezurek.texasholdem.server.Server;

public class Program {
	static final String DEFAULT_PORT = "7777";
	static final String DEFAULT_LOCALHOST = "127.0.0.1";

	public static void main(String[] args) {
		try {
			if(args.length == 0) {
				Logger.error("Invalid number of arguments");
				return;
			}
			
			String typ = args[0];

			//Wstawianie domyślnych argumentów, jeśli brak innych
			if(args.length == 1) {
				if(typ.equals("-s") || typ.equals("--server")){
					String[] default_args = new String[3];
					default_args[0] = args[0];
					default_args[1] = DEFAULT_PORT;
					args = default_args;
				}
				else if(typ.equals("-c") || typ.equals("--client")){
					String[] default_args = new String[3];
					default_args[0] = args[0];
					default_args[1] = DEFAULT_LOCALHOST;
					default_args[2] = DEFAULT_PORT;
					args = default_args;
				}

			}
			
			if(typ.equals("-s") || typ.equals("--server")) {
				if(args.length != 2) {
					Logger.error("Invalid number of arguments");
					return;
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
