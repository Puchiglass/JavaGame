package ru.example.quoridor.server;

import lombok.Getter;
import ru.example.quoridor.messages.PaintingLine;
import ru.example.quoridor.messages.PlayersMove;
import ru.example.quoridor.messages.Ready;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private final Game game = new Game();
    private final SocketServer socket = new SocketServer(this);
    private final Map<Integer, Player> playerIdByPort = new HashMap<>();

    @Getter
    private final List<Player> players = new ArrayList<>();

    public void runServer() {
        socket.waitNewPlayers();
    }

    public void addNewPlayer(ClientConnection connection) {
        this.playerIdByPort.put(connection.getPort(), this.game.newPlayer(connection));
    }

    public void processReadyMsg(int port) {
        playerIdByPort.get(port)
                .setReady(true);
        if (playerIdByPort.values().stream().allMatch(Player::isReady) && playerIdByPort.size() == 2) {
            this.playerIdByPort.values().forEach(player -> player.startGame(game.getCurMoveId(), players.size()));
        }
    }

    public void reset() {
        this.players.forEach(player -> player.setReady(false));
        this.game.reset();
    }

    public void processPaintLineMsg(int port, PlayersMove move) {
        int painterId = game.getCurMoveId();
        PaintLineResult result = game.move(playerIdByPort.get(port), move);
        if (result.type == PaintLineResultType.NEW_COLORED_CELLS) {
            updateGameStatusForPlayers(painterId, move, result.colored_cells);
        } else if (result.type == PaintLineResultType.FINISH) {
            giveFinishResultToPlayers(painterId, move, result.colored_cells);
            reset();
        }
    }

    public void processPlayersMove(PlayersMove move, int port) {
        int painterId = game.getCurMoveId();
        PaintLineResult result = game.move(playerIdByPort.get(port), move);
        if (result.type == PaintLineResultType.NEW_COLORED_CELLS) {
            int cur_move_id = game.getCurMoveId();
            this.playerIdByPort.values()
                    .forEach(player -> player.updateGameStatus(painterId, move, result.colored_cells, cur_move_id));
        } else if (result.type == PaintLineResultType.FINISH) {
            giveFinishResultToPlayers(painterId, move, result.colored_cells);
            reset();
        }
    }

    public void setReady(int port) {
        players.get(playerIdByPort.get(port).getId()).setReady(true);
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

    public void updateGameStatusForPlayers(int painter_id, PlayersMove move, List<Integer> cells) {
        int cur_move_id = game.getCurMoveId();
        this.players.forEach(player -> player.updateGameStatus(painter_id, move, cells, cur_move_id));
    }

    public void giveFinishResultToPlayers(int painter_id, PlayersMove move, List<Integer> cells) {
        int winnerId = 0;
        int maxScore = 0;
        List<Integer> score = game.getPlayerScore();
        for (int i = 0; i < score.size(); i++) {
            if (score.get(i) > maxScore) {
                winnerId = i;
                maxScore = score.get(i);
            }
        }

        for (Player player : players) {
            player.giveFinishResult(painter_id, move, cells, score, winnerId);
        }
    }

    public boolean canBeAddedPlayer() {
        return players.size() < 2;
    }
}
