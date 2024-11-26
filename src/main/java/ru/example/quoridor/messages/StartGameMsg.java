package org.corridor_game.corridor_game.messages;

import java.io.Serializable;

public class StartGameMsg implements Serializable {
    boolean is_cur_move;
    int num_players;

    public StartGameMsg(boolean is_cur_move_, int num_players_) {
        is_cur_move = is_cur_move_;
        num_players = num_players_;
    }

    public boolean isCurMove() {
        return is_cur_move;
    }

    public int getNumPlayers() {
        return num_players;
    }
}
