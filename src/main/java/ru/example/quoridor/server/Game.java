package ru.example.quoridor.server;

import lombok.Getter;
import ru.example.quoridor.model.LineType;
import ru.example.quoridor.model.PaintLineResult;
import ru.example.quoridor.model.PaintLineResultType;
import ru.example.quoridor.model.PlayersMove;

import java.util.ArrayList;
import java.util.List;

import static ru.example.quoridor.property.Property.FIELD_SIZE;
import static ru.example.quoridor.property.Property.TOTAL_CELLS;

public class Game {

    @Getter
    private final List<Integer> playerScore = new ArrayList<>();
    private final boolean[] verticalLines = new boolean[FIELD_SIZE * (FIELD_SIZE + 1)];
    private final boolean[] horizontalLines = new boolean[FIELD_SIZE * (FIELD_SIZE + 1)];
    private final int[] cells = new int[FIELD_SIZE * FIELD_SIZE];

    private int numColoredCells = 0;
    @Getter
    private int curMoveId = 0;

    public Player newPlayer(ClientConnection connection) {
        playerScore.add(0);
        return new Player(playerScore.size() - 1, connection);
    }

    private void markCell(int cellInd, int playerId, List<Integer> markedCells) {
        markedCells.add(cellInd);
        playerScore.set(playerId, playerScore.get(playerId) + 1);
        numColoredCells++;
    }

    public PaintLineResult move(Player player, PlayersMove move) {
        int playerId = player.getId();
        int lineId = move.id();
        if (lineId >= verticalLines.length || lineId < 0
                || (move.type() == LineType.HORIZONTAL && horizontalLines[lineId])
                || (move.type() == LineType.VERTICAL && verticalLines[lineId])
                || playerId < 0 || playerId >= playerScore.size() || playerId != curMoveId)
            return new PaintLineResult(PaintLineResultType.ERROR);

        List<Integer> markedCells = new ArrayList<>();
        switch (move.type()) {
            case HORIZONTAL -> handleHorizontalMove(lineId, playerId, markedCells);
            case VERTICAL -> handleVerticalMove(lineId, playerId, markedCells);
        }
        if (markedCells.isEmpty())
            curMoveId = (curMoveId + 1) % playerScore.size();

        if (numColoredCells == TOTAL_CELLS)
            return new PaintLineResult(PaintLineResultType.FINISH, markedCells);

        return new PaintLineResult(PaintLineResultType.NEW_COLORED_CELLS, markedCells);
    }

    private void handleVerticalMove(int lineId, int playerId, List<Integer> markedCells) {
        verticalLines[lineId] = true;
        if (lineId % 4 == 0) {
            if (++cells[lineId / 4 * 3] == 4)
                markCell(lineId / 4 * 3, playerId, markedCells);
        } else if (lineId % 4 == 3) {
            if (++cells[lineId - 1 - lineId / 4] == 4)
                markCell(lineId - 1 - lineId / 4, playerId, markedCells);
        } else {
            if (++cells[lineId - lineId / 4] == 4)
                markCell(lineId - lineId / 4, playerId, markedCells);
            if (++cells[lineId - 1 - lineId / 4] == 4)
                markCell(lineId - 1 - lineId / 4, playerId, markedCells);
        }
    }

    private void handleHorizontalMove(int lineId, int playerId, List<Integer> markedCells) {
        horizontalLines[lineId] = true;
        if (lineId < cells.length && ++cells[lineId] == 4)
            markCell(lineId, playerId, markedCells);

        if (lineId - FIELD_SIZE >= 0 && ++cells[lineId - FIELD_SIZE] == 4)
            markCell(lineId - 3, playerId, markedCells);
    }
}
