package ru.example.quoridor.client;

import lombok.Setter;
import ru.example.quoridor.messages.FinishGameMsg;
import ru.example.quoridor.messages.LineType;
import ru.example.quoridor.messages.StartGameMsg;
import ru.example.quoridor.messages.UpdateGameStatusMsg;

@Setter
public class ClientManager {

    private Controller controller;

    private SocketClient socket;

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
        controller.updateGameStatus(msg);
    }

    public void sendLine(int id, LineType type) {
        socket.sendLine(id, type);
    }

}
