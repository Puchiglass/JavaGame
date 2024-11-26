package org.corridor_game.corridor_game.messages;

import java.io.Serializable;

public class PaintingLine implements Serializable {
    public LineType type;
    public int index;

    public PaintingLine(int index, LineType type) {
        this.index = index;
        this.type = type;
    }
}
