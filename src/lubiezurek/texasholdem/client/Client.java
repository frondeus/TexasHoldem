package lubiezurek.texasholdem.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import lubiezurek.texasholdem.Logger;
import lubiezurek.texasholdem.json.JSONClientMessageFactory;
import lubiezurek.texasholdem.json.JSONServerMessageFactory;
import lubiezurek.texasholdem.server.IServerMessageFactory;
import lubiezurek.texasholdem.server.ServerMessage;
import lubiezurek.texasholdem.server.ServerResponse;

public class Client {
    private volatile static Client instance;

    public static Client getInstance() {
        if(instance == null) {
            synchronized (Client.class) {
                if(instance == null) instance = new Client();
            }
        }
        return instance;
    }

	private Socket socket;
    private DataOutputStream out;
    private IClientMessageFactory clientMessageFactory;
    private IServerMessageFactory serverMessageFactory;
    private ClientThread clientThread;

	private Client() {
        clientMessageFactory = new JSONClientMessageFactory();
        serverMessageFactory = new JSONServerMessageFactory();
	}
	
	public void run(String[] args) throws IOException {
        String address = args[1];
        int port = Integer.parseInt(args[2]);
        Logger.status("Connecting to: " + address + ":" + port);

        socket = new Socket(address, port);
        out = new DataOutputStream(socket.getOutputStream());

        clientThread = new ClientThread(this);
        clientThread.start();

		try {
			String line;
            String[] arguments;
			do {
				line = System.console().readLine();
                arguments = line.split("\\s+");

                IClientMessage message = clientMessageFactory
                        .createMessage()
                        .setCommand(arguments[0])
                        .setArguments(Arrays.copyOfRange(arguments, 1, arguments.length));
                out.writeUTF(message.toString());
			}
			while(!arguments[0].equals("exit") && clientThread.isAlive());
			
			Logger.status("Exiting");
			socket.close();
		}
		catch (IOException e) {
			Logger.exception(e);
		}
        this.clientThread.interrupt();
	}

	public Socket getSocket() {
		return socket;
	}

	public void onMessage(ServerMessage message) {
        if(message.getType() == ServerMessage.Type.Response) {
            ServerResponse response = (ServerResponse) message;
            Logger.status(response.getStatus() + ": " + response.getMessage());
        }
	}

    public IServerMessageFactory getServerMessageFactory() {
        return serverMessageFactory;
    }
}
