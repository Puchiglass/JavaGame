package org.corridor_game.corridor_game.server;

import java.util.ArrayList;

public class PaintLineResult {
    public PaintLineResultType type;
    public ArrayList<Integer> colored_cells;

    PaintLineResult(PaintLineResultType type_) {
        type = type_;
        colored_cells = new ArrayList<>();
    }

    PaintLineResult(PaintLineResultType type_, ArrayList<Integer> colored_cells_) {
        type = type_;
        colored_cells = colored_cells_;
    }
}
