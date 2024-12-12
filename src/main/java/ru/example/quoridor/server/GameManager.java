package ru.example.quoridor.server;

import ru.example.quoridor.model.PaintLineResult;
import ru.example.quoridor.model.PlayersMove;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private final Game game = new Game();
    private final SocketServer socket = new SocketServer(this);
    private final Map<Integer, Player> playerIdByPort = new HashMap<>();

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
            this.playerIdByPort.values().forEach(player -> player.startGame(game.getCurMoveId()));
        }
    }

    public void processPlayersMove(PlayersMove move, int port) {
        int painterId = game.getCurMoveId();
        PaintLineResult result = game.move(playerIdByPort.get(port), move);
        switch (result.type) {
            case NEW_COLORED_CELLS -> this.playerIdByPort.values()
                    .forEach(player -> player.updateGameStatus(painterId, move, result.markedCells, game.getCurMoveId()));
            case FINISH -> giveFinishResultToPlayers(painterId, move, result.markedCells);
        }
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

        int finalWinnerId = winnerId;
        playerIdByPort.values().forEach(player -> player.finishGame(painter_id, move, cells, score, finalWinnerId));
    }

    public boolean canBeAddedPlayer() {
        return playerIdByPort.values().size() < 2;
    }
}
