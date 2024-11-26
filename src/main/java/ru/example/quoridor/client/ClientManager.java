package org.corridor_game.corridor_game.client;

import org.corridor_game.corridor_game.messages.FinishGameMsg;
import org.corridor_game.corridor_game.messages.LineType;
import org.corridor_game.corridor_game.messages.StartGameMsg;
import org.corridor_game.corridor_game.messages.UpdateGameStatusMsg;

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
