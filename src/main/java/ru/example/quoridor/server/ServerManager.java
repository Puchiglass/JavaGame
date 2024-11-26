package org.corridor_game.corridor_game.server;

import org.corridor_game.corridor_game.messages.PaintingLine;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerManager {
    SocketServer socket;
    Game game;
    ArrayList<Player> players;
    HashMap<Integer, Integer> player_id_by_port;

    ServerManager() {
        players = new ArrayList<>();
        player_id_by_port = new HashMap<>();
        game = new Game(this);
        socket = new SocketServer(this);
    }

    public void runServer() {
        socket.waitNewPlayers();
    }

    public void addNewPlayer(ClientConnection connection) {
        int id = game.addNewPlayer();
        player_id_by_port.put(connection.getPort(), id);
        players.add(new Player(id, connection));
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void processReadyMsg(int port) {
        setReady(port);
        tryStartGame();
    }

    void reset() {
        for (Player player : players) {
            player.setReady(false);
        }
        game.reset();
    }

    public void processPaintLineMsg(int port, PaintingLine line) {
        int painter_id = game.getCurMoveId();
        PaintLineResult result = game.paintLine(player_id_by_port.get(port), line);
        if (result.type == PaintLineResultType.NEW_COLORED_CELLS) {
            updateGameStatusForPlayers(painter_id, line, result.colored_cells);
        }
        else if (result.type == PaintLineResultType.FINISH) {
            giveFinishResultToPlayers(painter_id, line, result.colored_cells);
            reset();
        }
    }

    public void setReady(int port) {
        int id = player_id_by_port.get(port);
        players.get(id).setReady(true);
    }

    void tryStartGame() {
        for (Player player : players) {
            if (!player.isReady()) {
                return;
            }
        }
        int cur_move_id = game.getCurMoveId();
        for (Player player : players) {
            player.startGame(cur_move_id, players.size());
        }
    }

    void updateGameStatusForPlayers(int painter_id, PaintingLine line, ArrayList<Integer> cells) {
        int cur_move_id = game.getCurMoveId();
        for (Player player : players) {
            player.updateGameStatus(painter_id, line, cells, cur_move_id);
        }
    }

    void giveFinishResultToPlayers(int painter_id, PaintingLine line, ArrayList<Integer> cells) {
        int winner_id = 0;
        int max_score = 0;
        ArrayList<Integer> score = game.getPlayerScore();
        for (int i = 0; i < score.size(); i++) {
            if (score.get(i) > max_score) {
                winner_id = i;
                max_score = score.get(i);
            }
        }

        for (Player player : players) {
            player.giveFinishResult(painter_id, line, cells, score, winner_id);
        }
    }
}
