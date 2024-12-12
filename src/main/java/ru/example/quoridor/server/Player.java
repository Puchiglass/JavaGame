package ru.example.quoridor.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.example.quoridor.messages.PlayersMove;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class Player {

    private ClientConnection connection;
    private int id;
    private int score = 0;

    public Player(int id, ClientConnection connection) {
        this.id = id;
        this.connection = connection;
    }

    private boolean isReady = false;

    public void startGame(int curMoveId, int numPlayers) {
        connection.startGame(curMoveId == this.id, numPlayers);
    }

    public void updateGameStatus(int painterId, PlayersMove move, List<Integer> cells, int curMoveId) {
        connection.sendUpdateGameStatus(painterId, move, cells, curMoveId == id);
    }

    public void giveFinishResult(int painterId, PlayersMove move, List<Integer> cells,
                                 List<Integer> score, int winnerId) {
        connection.sendFinishResult(painterId, move, cells, score, winnerId == id);
    }

}
