package ru.example.quoridor.client;

import lombok.Setter;
import ru.example.quoridor.model.*;

@Setter
public class ClientManager {

    private SocketClient socket;
    private CorridorGame corridorGame;

    // Singleton
    private ClientManager() {
    }
    private static class Holder {
        private static final ClientManager INSTANCE = new ClientManager();
    }
    public static ClientManager getInstance() {
        return Holder.INSTANCE;
    }

    public void ready() {
        socket.sendReady(new Ready());
    }

    public void sendLine(int id, LineType type) {
        socket.sendLine(id, type);
    }

    public void startGame(Start msg) {
        corridorGame.start(msg);
    }

    public void updateGame(Update msg) {
        corridorGame.update(msg);
    }

    public void finishGame(Finish msg) {
        corridorGame.finish(msg);
    }

}
