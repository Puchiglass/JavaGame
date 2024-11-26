package org.corridor_game.corridor_game.server;

import org.corridor_game.corridor_game.messages.PaintingLine;

import java.util.ArrayList;

public class Player {
    private int id;
    private boolean is_ready;
    ClientConnection connection;

    Player(int id_, ClientConnection connection_) {
        id = id_;
        is_ready = false;
        connection = connection_;
    }

    public void setReady(boolean value) {
        is_ready = value;
    }

    public boolean isReady() {
        return is_ready;
    }

    public void startGame(int cur_move_id, int num_players) {
        connection.sendStartGame(cur_move_id == id, num_players);
    }

    public void updateGameStatus(int painter_id, PaintingLine line, ArrayList<Integer> cells, int cur_move_id) {
        connection.sendUpdateGameStatus(painter_id, line, cells, cur_move_id == id);
    }

    public void giveFinishResult(int painter_id, PaintingLine line, ArrayList<Integer> cells,
                                 ArrayList<Integer> score, int winner_id) {
        connection.sendFinishResult(painter_id, line, cells, score, winner_id == id);
    }
}
