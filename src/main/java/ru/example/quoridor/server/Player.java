package ru.example.quoridor.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.example.quoridor.model.PlayersMove;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class Player {

    private ClientConnection connection;
    private int id;
    private boolean isReady = false;

    public Player(int id, ClientConnection connection) {
        this.id = id;
        this.connection = connection;
    }

    public void startGame(int curMoveId) {
        connection.startGame(curMoveId == this.id);
    }

    public void updateGameStatus(int painterId, PlayersMove move, List<Integer> cells, int curMoveId) {
        connection.updateGame(painterId, move, cells, curMoveId == id);
    }

    public void finishGame(int painterId, PlayersMove move, List<Integer> cells, List<Integer> score, int winnerId) {
        connection.finishGame(painterId, move, cells, score, winnerId == id);
    }

}
