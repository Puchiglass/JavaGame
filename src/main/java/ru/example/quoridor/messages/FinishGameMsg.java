package org.corridor_game.corridor_game.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class FinishGameMsg implements Serializable {
    int painter_id;
    PaintingLine line;
    ArrayList<Integer> cells;
    ArrayList<Integer> score;
    boolean is_winner;

    public FinishGameMsg(int painter_id_, PaintingLine line_, ArrayList<Integer> cells_, ArrayList<Integer> score_, boolean is_winner_) {
        painter_id = painter_id_;
        line = line_;
        cells = cells_;
        score = score_;
        is_winner = is_winner_;
    }

    public int getPainterId() {
        return painter_id;
    }

    public ArrayList<Integer> getCells() {
        return cells;
    }

    public PaintingLine getLine() {
        return line;
    }

    public ArrayList<Integer> getScore() {
        return score;
    }

    public boolean isWinner() {
        return is_winner;
    }
}
