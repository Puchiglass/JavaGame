package ru.example.quoridor.messages;

import java.io.Serializable;

public record PlayersMove(LineType type, int id) implements Serializable {
}
