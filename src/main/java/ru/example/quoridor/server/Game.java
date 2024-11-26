package org.corridor_game.corridor_game.server;

import org.corridor_game.corridor_game.messages.LineType;
import org.corridor_game.corridor_game.messages.PaintingLine;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    ServerManager manager;
    static final int field_size = 3;
    static final int total_cells = field_size * field_size;
    int num_colored_cells = 0;
    int cur_move_id = 0;
    int[] cells = new int[field_size * field_size];
    boolean[] vertical_lines = new boolean[field_size * (field_size + 1)];
    boolean[] horizontal_lines = new boolean[field_size * (field_size + 1)];
    ArrayList<Integer> player_score;
    Game(ServerManager manager_) {
        manager = manager_;
        player_score = new ArrayList<>();
    }

    int addNewPlayer() {
        player_score.add(0);
        return player_score.size() - 1;
    }

    public void reset() {
        cur_move_id = 0;
        num_colored_cells = 0;
        Arrays.fill(vertical_lines, false);
        Arrays.fill(horizontal_lines, false);
        Arrays.fill(cells, 0);
        player_score.replaceAll(ignored -> 0);

    }

    PaintLineResult paintLine(int player_id, PaintingLine line) {
        if (line.index < 0 || line.index >= vertical_lines.length ||
                (line.type == LineType.VERTICAL && vertical_lines[line.index]) ||
                (line.type == LineType.HORIZONTAL && horizontal_lines[line.index])) {
            return new PaintLineResult(PaintLineResultType.INVALID_LINE);
        }
        if (player_id < 0 || player_id >= player_score.size() || player_id != cur_move_id) {
            return new PaintLineResult(PaintLineResultType.INVALID_PLAYER);
        }

        if (line.type == LineType.VERTICAL) {
            vertical_lines[line.index] = true;
        }
        else {
            horizontal_lines[line.index] = true;
        }

        ArrayList<Integer> colored_cells = new ArrayList<>();
        if (line.type == LineType.HORIZONTAL) {
            if (line.index < cells.length && ++cells[line.index] == 4) {
                colorCell(line.index, player_id, colored_cells);
            }

            if (line.index - field_size >= 0 && ++cells[line.index - field_size] == 4) {
                colorCell(line.index - 3, player_id, colored_cells);
            }
        }
        else {
            if (line.index % 4 == 0) {
                if (++cells[line.index / 4 * 3] == 4) {
                    colorCell(line.index / 4 * 3, player_id, colored_cells);
                }
            }
            else if (line.index % 4 == 3) {
                if (++cells[line.index - 1 - line.index / 4] == 4) {
                    colorCell(line.index - 1 - line.index / 4, player_id, colored_cells);
                }
            }
            else {
                if (++cells[line.index - line.index / 4] == 4) {
                    colorCell(line.index - line.index / 4, player_id, colored_cells);
                }
                if (++cells[line.index - 1 - line.index / 4] == 4) {
                    colorCell(line.index - 1 - line.index / 4, player_id, colored_cells);
                }
            }
        }

        if (colored_cells.isEmpty()) {
            cur_move_id = (cur_move_id + 1) % player_score.size();
        }

        if (num_colored_cells == total_cells) {
            return new PaintLineResult(PaintLineResultType.FINISH, colored_cells);
        }
        return new PaintLineResult(PaintLineResultType.NEW_COLORED_CELLS, colored_cells);
    }

    private void colorCell(int cell_ind, int player_id, ArrayList<Integer> colored_cells) {
        colored_cells.add(cell_ind);
        player_score.set(player_id, player_score.get(player_id) + 1);
        num_colored_cells++;
    }

    ArrayList<Integer> getPlayerScore() {
        return player_score;
    }

    public int getCurMoveId() {
        return cur_move_id;
    }
}
