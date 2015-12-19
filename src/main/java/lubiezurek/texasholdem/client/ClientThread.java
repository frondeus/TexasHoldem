package main.java.lubiezurek.texasholdem.client;

import main.java.lubiezurek.texasholdem.Logger;
import main.java.lubiezurek.texasholdem.server.ServerMessage;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by frondeus on 06.12.2015.
 * Thread reading from socket.
 */
class ClientThread extends Thread {

    private final DataInputStream in;
    private final Client client;

    public ClientThread(Client client) throws IOException {
        this.client = client;
        Socket socket = client.getSocket();
        this.in = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while(true) {
                String input = this.in.readUTF();

                ServerMessage message = this.client.getServerMessageBuilder()
                        .deserializeMessage(input);
                this.client.onMessage(message);
            }
        }
        catch (EOFException e) {
            Logger.status("EOF");
        }
        catch (IOException e) {
            Logger.exception(e);
        }
    }
}
