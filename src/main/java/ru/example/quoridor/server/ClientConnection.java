package ru.example.quoridor.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.example.quoridor.client.Finish;
import ru.example.quoridor.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientConnection {

    private static final Logger log = LogManager.getLogger(ClientConnection.class);

    private final Socket socket;
    private final GameManager manager;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientConnection(Socket socket, GameManager manager) {
        this.socket = socket;
        this.manager = manager;
        try {
            oos = new ObjectOutputStream(this.socket.getOutputStream());
            ois = new ObjectInputStream(this.socket.getInputStream());
        }
        catch (IOException e) {
            log.error("Error receiving streams on port %d".formatted(this.socket.getPort()));
        }
        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    public int getPort() {
        return socket.getPort();
    }

    void run() {
        while (true) {
            try {
                Object obj = ois.readObject();
                if (obj instanceof Ready) {
                    manager.processReadyMsg(getPort());
                }
                if (obj instanceof PlayersMove move) {
                    manager.processPlayersMove(move, getPort());
                }
            }
            catch (IOException e) {
                log.error("Client disconnect");
                break;
            }
            catch (ClassNotFoundException e) {
                log.error(e.getMessage());
            }
        }
    }

    void startGame(boolean isCurMove, int numPlayers) {
        try {
            oos.writeObject(new Start(isCurMove, numPlayers));
        }
        catch (IOException e) {
            log.error("Failed to send the start message on port - %d".formatted(getPort()));
        }
    }

    void sendUpdateGameStatus(int painterId, PlayersMove move, List<Integer> cells, boolean isCurMove) {
        try {
            oos.writeObject(new Update(painterId, move, cells, isCurMove));
        }
        catch (IOException e) {
            log.error("Failed to send update field on port - %d".formatted(getPort()));
        }
    }

    void sendFinishResult(int painterId, PlayersMove move, List<Integer> cells,
                          List<Integer> score, boolean isWinner) {
        try {
            oos.writeObject(new Finish(painterId, isWinner, move, cells, score));
            oos.reset();
        }
        catch (IOException e) {
            log.error("Failed to send finish result on port - %d".formatted(getPort()));
        }
    }
}
