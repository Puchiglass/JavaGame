package ru.example.quoridor.messages;

import java.io.Serializable;

public class PaintingLine implements Serializable {
    public LineType type;
    public int index;

    public PaintingLine(int index, LineType type) {
        this.index = index;
        this.type = type;
    }
}
