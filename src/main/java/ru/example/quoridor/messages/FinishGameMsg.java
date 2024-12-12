package ru.example.quoridor.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record FinishGameMsg(
        int painterId,
        boolean isWinner,
        PlayersMove move,
        List<Integer> cells,
        List<Integer> score
) implements Serializable {
}
