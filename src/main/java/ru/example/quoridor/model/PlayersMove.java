package ru.example.quoridor.model;

import java.io.Serializable;

public record PlayersMove(LineType type, int id) implements Serializable {
}
