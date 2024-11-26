package ru.example.quoridor.server;

import lombok.Getter;
import ru.example.quoridor.messages.PaintingLine;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerManager {

    private final Game game = new Game();
    private final SocketServer socket = new SocketServer(this);
    private final HashMap<Integer, Integer> playerIdByPort = new HashMap<>();

    @Getter
    private ArrayList<Player> players = new ArrayList<>();

    public void runServer() {
        socket.waitNewPlayers();
    }

    public void addNewPlayer(ClientConnection connection) {
        int id = game.addNewPlayer();
        this.playerIdByPort.put(connection.getPort(), id);
        this.players.add(new Player(connection, id));
    }

    public void processReadyMsg(int port) {
        setReady(port);
        tryStartGame();
    }

    public void reset() {
        this.players.forEach(player -> player.setReady(false));
        this.game.reset();
    }

    public void processPaintLineMsg(int port, PaintingLine line) {
        int painterId = game.getCurMoveId();
        PaintLineResult result = game.paintLine(playerIdByPort.get(port), line);
        if (result.type == PaintLineResultType.NEW_COLORED_CELLS) {
            updateGameStatusForPlayers(painterId, line, result.colored_cells);
        }
        else if (result.type == PaintLineResultType.FINISH) {
            giveFinishResultToPlayers(painterId, line, result.colored_cells);
            reset();
        }
    }

    public void setReady(int port) {
        int id = playerIdByPort.get(port);
        players.get(id).setReady(true);
    }

    public void tryStartGame() {
        for (Player player : players) {
            if (!player.isReady()) {
                return;
            }
        }
        int cur_move_id = game.getCurMoveId();
        this.players.forEach(player -> player.startGame(cur_move_id, players.size()));
    }

    public void updateGameStatusForPlayers(int painter_id, PaintingLine line, ArrayList<Integer> cells) {
        int cur_move_id = game.getCurMoveId();
        this.players.forEach(player -> player.updateGameStatus(painter_id, line, cells, cur_move_id));
    }

    public void giveFinishResultToPlayers(int painter_id, PaintingLine line, ArrayList<Integer> cells) {
        int winnerId = 0;
        int maxScore = 0;
        ArrayList<Integer> score = game.getPlayerScore();
        for (int i = 0; i < score.size(); i++) {
            if (score.get(i) > maxScore) {
                winnerId = i;
                maxScore = score.get(i);
            }
        }

        for (Player player : players) {
            player.giveFinishResult(painter_id, line, cells, score, winnerId);
        }
    }
}
