package ru.example.quoridor.server;

import lombok.Getter;
import ru.example.quoridor.messages.LineType;
import ru.example.quoridor.messages.PaintingLine;
import ru.example.quoridor.messages.PlayersMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Game {
    private static final int FIELD_SIZE = 3;
    private static final int TOTAL_CELLS = FIELD_SIZE * FIELD_SIZE;

    @Getter
    private final List<Integer> playerScore = new ArrayList<>();
    ;
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

    public void reset() {
        curMoveId = 0;
        numColoredCells = 0;
        Arrays.fill(verticalLines, false);
        Arrays.fill(horizontalLines, false);
        Arrays.fill(cells, 0);
        Collections.fill(playerScore, 0);

    }

    private void colorCell(int cellInd, int playerId, List<Integer> coloredCells) {
        coloredCells.add(cellInd);
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

        List<Integer> coloredCells = new ArrayList<>();
        switch (move.type()) {
            case HORIZONTAL -> handleHorizontalMove(lineId, playerId, coloredCells);
            case VERTICAL -> handleVerticalMove(lineId, playerId, coloredCells);
        }
        if (coloredCells.isEmpty())
            curMoveId = (curMoveId + 1) % playerScore.size();

        if (numColoredCells == TOTAL_CELLS)
            return new PaintLineResult(PaintLineResultType.FINISH, coloredCells);

        return new PaintLineResult(PaintLineResultType.NEW_COLORED_CELLS, coloredCells);
    }

    private void handleVerticalMove(int lineId, int playerId, List<Integer> coloredCells) {
        verticalLines[lineId] = true;
        if (lineId % 4 == 0) {
            if (++cells[lineId / 4 * 3] == 4)
                colorCell(lineId / 4 * 3, playerId, coloredCells);
        } else if (lineId % 4 == 3) {
            if (++cells[lineId - 1 - lineId / 4] == 4)
                colorCell(lineId - 1 - lineId / 4, playerId, coloredCells);
        } else {
            if (++cells[lineId - lineId / 4] == 4)
                colorCell(lineId - lineId / 4, playerId, coloredCells);
            if (++cells[lineId - 1 - lineId / 4] == 4)
                colorCell(lineId - 1 - lineId / 4, playerId, coloredCells);
        }
    }

    private void handleHorizontalMove(int lineId, int playerId, List<Integer> coloredCells) {
        horizontalLines[lineId] = true;
        if (lineId < cells.length && ++cells[lineId] == 4) {
            colorCell(lineId, playerId, coloredCells);
        }

        if (lineId - FIELD_SIZE >= 0 && ++cells[lineId - FIELD_SIZE] == 4) {
            colorCell(lineId - 3, playerId, coloredCells);
        }
    }
}
