package main.java.lubiezurek.texasholdem.client;

import main.java.lubiezurek.texasholdem.Logger;
import main.java.lubiezurek.texasholdem.json.JSONClientMessageBuilder;
import main.java.lubiezurek.texasholdem.json.JSONServerMessageBuilder;
import main.java.lubiezurek.texasholdem.server.IServerMessageBuilder;
import main.java.lubiezurek.texasholdem.server.ServerEvent;
import main.java.lubiezurek.texasholdem.server.ServerMessage;
import main.java.lubiezurek.texasholdem.server.ServerResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

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
    private IClientMessageBuilder clientMessageBuilder;
    private IServerMessageBuilder serverMessageBuilder;
    private ClientThread clientThread;

	private Client() {
        clientMessageBuilder = new JSONClientMessageBuilder();
        serverMessageBuilder = new JSONServerMessageBuilder();
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
                System.out.print('>');
				line = System.console().readLine();
                arguments = line.split("\\s+");

                if(arguments[0].equals("exit")) break;

                ClientMessage message = new ClientMessage()
                        .setCommand(arguments[0])
                        .setArguments(Arrays.copyOfRange(arguments, 1, arguments.length));
                out.writeUTF(clientMessageBuilder.serializeMessage(message));
			}
			while(clientThread.isAlive());
			
			Logger.status("Exiting");
            this.clientThread.interrupt();
            this.clientThread.join();

			socket.close();
		}
		catch (IOException e) {
			Logger.exception(e);
		} catch (InterruptedException e) {
            Logger.exception(e);
        }
    }

	public Socket getSocket() {
		return socket;
	}

	public void onMessage(ServerMessage message) {
        if(message instanceof ServerResponse) {
            ServerResponse response = (ServerResponse) message;
            Logger.status(response.getStatus() + ": " + response.getMessage());
        }
        else if (message instanceof ServerEvent) {
            ServerEvent event = (ServerEvent) message;
            String args = String.join(" ", event.getArguments());
            Logger.status(event.getEventType().toString() + ": " + args);
        }
	}

    public IServerMessageBuilder getServerMessageBuilder() {
        return serverMessageBuilder;
    }
}
