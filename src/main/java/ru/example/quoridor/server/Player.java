package ru.example.quoridor.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.example.quoridor.messages.PaintingLine;

import java.util.ArrayList;

@RequiredArgsConstructor
public class Player {

    private final ClientConnection connection;
    private final int id;

    @Setter
    @Getter
    private boolean isReady = false;

    public void startGame(int curMoveId, int numPlayers) {
        connection.sendStartGame(curMoveId == id, numPlayers);
    }

    public void updateGameStatus(int painterId, PaintingLine line, ArrayList<Integer> cells, int curMoveId) {
        connection.sendUpdateGameStatus(painterId, line, cells, curMoveId == id);
    }

    public void giveFinishResult(int painterId, PaintingLine line, ArrayList<Integer> cells,
                                 ArrayList<Integer> score, int winnerId) {
        connection.sendFinishResult(painterId, line, cells, score, winnerId == id);
    }

}
