package ru.example.quoridor.messages;

import java.io.Serializable;

public record Start(boolean isCurMove, int numPlayers) implements Serializable {
}
