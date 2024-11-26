package org.corridor_game.corridor_game.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateGameStatusMsg implements Serializable {
    int painter_id;
    PaintingLine line;
    ArrayList<Integer> cells;
    boolean is_cur_move;

    public UpdateGameStatusMsg(int painter_id_, PaintingLine line_, ArrayList<Integer> cells_, boolean is_cur_move_) {
        painter_id = painter_id_;
        line = line_;
        cells = cells_;
        is_cur_move = is_cur_move_;
    }

    public PaintingLine getLine() {
        return line;
    }

    public ArrayList<Integer> getCells() {
        return cells;
    }

    public boolean isCurMove() {
        return is_cur_move;
    }

    public int getPainterId() {
        return painter_id;
    }
}
