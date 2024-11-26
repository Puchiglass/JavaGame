package ru.example.quoridor.client;

import ru.example.quoridor.messages.FinishGameMsg;
import ru.example.quoridor.messages.LineType;
import ru.example.quoridor.messages.StartGameMsg;
import ru.example.quoridor.messages.UpdateGameStatusMsg;

public class ClientManager {
    VisualController controller;
    SocketClient socket;

    public void setController(VisualController controller) {
        this.controller = controller;
    }

    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }

    public void sendReady() {
        socket.sendReady();
    }

    public void startGame(StartGameMsg msg) {
        controller.startGame(msg);
    }

    public void finishGame(FinishGameMsg msg) {
        controller.finishGame(msg);
    }

    public void updateGameStatus(UpdateGameStatusMsg msg) {
        controller.UpdateGameStatus(msg);
    }

    public void sendLine(int id, LineType type) {
        socket.sendLine(id, type);
    }
}
