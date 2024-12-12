package ru.example.quoridor.model;

import java.io.Serializable;
import java.util.List;

public record Finish(
        int painterId,
        boolean isWinner,
        PlayersMove move,
        List<Integer> cells,
        List<Integer> score
) implements Serializable {
}
