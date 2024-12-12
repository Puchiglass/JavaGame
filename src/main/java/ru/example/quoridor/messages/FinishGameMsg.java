package ru.example.quoridor.messages;

import java.io.Serializable;
import java.util.ArrayList;

public record FinishGameMsg(
        int painterId,
        boolean isWinner,
        PaintingLine line,
        ArrayList<Integer> cells, ArrayList<Integer> score
) implements Serializable {
}
