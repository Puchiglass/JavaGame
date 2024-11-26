package ru.example.quoridor.server;

import lombok.Getter;
import ru.example.quoridor.messages.LineType;
import ru.example.quoridor.messages.PaintingLine;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private static final int FIELD_SIZE = 3;
    private static final int TOTAL_CELLS = FIELD_SIZE * FIELD_SIZE;

    @Getter
    private final ArrayList<Integer> playerScore;
    private final boolean[] verticalLines = new boolean[FIELD_SIZE * (FIELD_SIZE + 1)];
    private final boolean[] horizontalLines = new boolean[FIELD_SIZE * (FIELD_SIZE + 1)];
    private final int[] cells = new int[FIELD_SIZE * FIELD_SIZE];

    private int numColoredCells = 0;
    @Getter
    private int curMoveId = 0;

    public Game() {
        playerScore = new ArrayList<>();
    }

    public int addNewPlayer() {
        playerScore.add(0);
        return playerScore.size() - 1;
    }

    public void reset() {
        curMoveId = 0;
        numColoredCells = 0;
        Arrays.fill(verticalLines, false);
        Arrays.fill(horizontalLines, false);
        Arrays.fill(cells, 0);
        playerScore.replaceAll(ignored -> 0);

    }

    public PaintLineResult paintLine(int playerId, PaintingLine line) {
        if (line.index < 0 || line.index >= verticalLines.length ||
                (line.type == LineType.VERTICAL && verticalLines[line.index]) ||
                (line.type == LineType.HORIZONTAL && horizontalLines[line.index])) {
            return new PaintLineResult(PaintLineResultType.INVALID_LINE);
        }
        if (playerId < 0 || playerId >= playerScore.size() || playerId != curMoveId) {
            return new PaintLineResult(PaintLineResultType.INVALID_PLAYER);
        }

        if (line.type == LineType.VERTICAL) {
            verticalLines[line.index] = true;
        }
        else {
            horizontalLines[line.index] = true;
        }

        ArrayList<Integer> coloredCells = new ArrayList<>();
        if (line.type == LineType.HORIZONTAL) {
            if (line.index < cells.length && ++cells[line.index] == 4) {
                colorCell(line.index, playerId, coloredCells);
            }

            if (line.index - FIELD_SIZE >= 0 && ++cells[line.index - FIELD_SIZE] == 4) {
                colorCell(line.index - 3, playerId, coloredCells);
            }
        }
        else {
            if (line.index % 4 == 0) {
                if (++cells[line.index / 4 * 3] == 4) {
                    colorCell(line.index / 4 * 3, playerId, coloredCells);
                }
            }
            else if (line.index % 4 == 3) {
                if (++cells[line.index - 1 - line.index / 4] == 4) {
                    colorCell(line.index - 1 - line.index / 4, playerId, coloredCells);
                }
            }
            else {
                if (++cells[line.index - line.index / 4] == 4) {
                    colorCell(line.index - line.index / 4, playerId, coloredCells);
                }
                if (++cells[line.index - 1 - line.index / 4] == 4) {
                    colorCell(line.index - 1 - line.index / 4, playerId, coloredCells);
                }
            }
        }

        if (coloredCells.isEmpty()) {
            curMoveId = (curMoveId + 1) % playerScore.size();
        }

        if (numColoredCells == TOTAL_CELLS) {
            return new PaintLineResult(PaintLineResultType.FINISH, coloredCells);
        }
        return new PaintLineResult(PaintLineResultType.NEW_COLORED_CELLS, coloredCells);
    }

    private void colorCell(int cellInd, int playerId, ArrayList<Integer> coloredCells) {
        coloredCells.add(cellInd);
        playerScore.set(playerId, playerScore.get(playerId) + 1);
        numColoredCells++;
    }

}
