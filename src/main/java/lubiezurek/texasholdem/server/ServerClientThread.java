package main.java.lubiezurek.texasholdem.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import main.java.lubiezurek.texasholdem.Logger;
import main.java.lubiezurek.texasholdem.client.ClientMessage;

public class ServerClientThread extends Thread implements IPlayer {
    private final Server server;
	private final Socket socket;
	private DataInputStream in; 
	private DataOutputStream out;
    private boolean isRunning;

    private int money = 100;
    private UUID uuid;

	public ServerClientThread(Server server, Socket socket) throws IOException {
		Logger.status("New connection: " + socket.getRemoteSocketAddress().toString());

        this.server = server;
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
        this.isRunning = true;
        this.uuid = UUID.randomUUID();
        server.getState().onClientConnected(this);
	}

    @Override
    public String toString() {
        return socket.getRemoteSocketAddress().toString();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }


    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public void setMoney(int money) {
        this.money = money;
    }

    public void sendMessage(ServerMessage message) throws IOException {
        out.writeUTF(this.server.getServerMessageBuilder().serializeMessage(message));
    }

    public void disconnect() {
        isRunning = false;
    }

	@Override
	public void run() {
        try {
            while(isRunning) {
                String input = in.readUTF();
                ClientMessage message = this.server.getClientMessageBuilder().deserializeMessage(input);
                server.getState().onClientMessage(this, message);
            }
            socket.close();
        }
        catch(EOFException exception) {
            Logger.status("EOF");
        }
        catch(IOException exception) {
            Logger.exception(exception);
        }

        server.getState().onClientDisconnected(this);
	}
}
