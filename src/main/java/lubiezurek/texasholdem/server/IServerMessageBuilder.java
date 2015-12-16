package main.java.lubiezurek.texasholdem.server;

/**
 * Created by frondeus on 06.12.2015.
 */
public interface IServerMessageBuilder {
    ServerMessage deserializeMessage(String input);
    String serializeMessage(ServerMessage message);
}
