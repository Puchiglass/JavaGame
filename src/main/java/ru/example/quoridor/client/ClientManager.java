package ru.example.quoridor.client;

import lombok.Setter;
import ru.example.quoridor.messages.*;

@Setter
public class ClientManager {

    private Controller controller;

    private SocketClient socket;

    private CorridorGame corridorGame;

    public void ready() {
        socket.sendReady(new Ready());
    }

    public void startGame(Start msg) {
        corridorGame.start(msg);
    }

    public void finishGame(FinishGameMsg msg) {
        controller.finishGame(msg);
    }

    public void updateGameStatus(Update msg) {
        corridorGame.update(msg);
    }

    public void sendLine(int id, LineType type) {
        socket.sendLine(id, type);
    }

    public void finishGame(Finish msg) {
        corridorGame.finish(msg);
    }

    public void ready(String text) {
        socket.sendReady(new Ready());
    }
}
