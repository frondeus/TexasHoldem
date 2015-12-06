package lubiezurek.texasholdem.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import lubiezurek.texasholdem.Logger;

public class ServerClientThread extends Thread {
    private final Server server;
	private final Socket socket;
	private DataInputStream in; 
	private DataOutputStream out;
    private boolean isRunning;

	public ServerClientThread(Server server, Socket socket) throws IOException {
		Logger.status("New connection: " + socket.getRemoteSocketAddress().toString());

        this.server = server;
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
        this.isRunning = true;

        server.getState().onClientConnected(this);
	}

    @Override
    public String toString() {
        return socket.getRemoteSocketAddress().toString();
    }

    public void sendMessage(ServerMessage message) throws IOException {
        out.writeUTF(message.toString());
    }

    public void disconnect() {
        isRunning = false;
    }

	@Override
	public void run() {
        try {
            while(isRunning) {
                String input = in.readUTF();
                server.getState().onClientMessage(this, this.server.getClientMessageFactory().createMessage(input));
            }
            socket.close();
        }
        catch(EOFException exception) {

        }
        catch(IOException exception) {
            Logger.exception(exception);
        }

        server.getState().onClientDisconnected(this);
	}
}
