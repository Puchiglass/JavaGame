package ru.example.quoridor.messages;

import java.io.Serializable;

public record PaintingLine(
        LineType type,
        int index
) implements Serializable {
}
// TODO перевести в record