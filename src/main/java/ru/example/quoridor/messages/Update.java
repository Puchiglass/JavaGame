package ru.example.quoridor.messages;

import java.io.Serializable;
import java.util.List;

public record Update (

    int painterId,
    PlayersMove move,
    List<Integer> cells,
    boolean isCurMove
) implements Serializable {
}
