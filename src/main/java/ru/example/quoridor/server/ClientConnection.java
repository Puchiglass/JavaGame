package ru.example.quoridor.server;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.example.quoridor.model.Finish;
import ru.example.quoridor.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientConnection {

    private static final Logger log = LogManager.getLogger(ClientConnection.class);

    @Getter
    private Socket socket = null;
    private GameManager manager;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ClientConnection(Socket socket, GameManager manager) {
        try {
            this.socket = socket;
            this.manager = manager;
            objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            Thread thread = new Thread(this::run);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            log.error("Error receiving streams on port %d".formatted(this.socket.getPort()));
        }
    }

    private void run() {
        while (true) {
            try {
                Object obj = objectInputStream.readObject();
                if (obj instanceof Ready) {
                    manager.processReadyMsg(socket.getPort());
                }
                if (obj instanceof PlayersMove move) {
                    manager.processPlayersMove(move, socket.getPort());
                }
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error("Client disconnect");
                break;
            }
        }
    }

    public void startGame(boolean isCurMove) {
        try {
            objectOutputStream.writeObject(new Start(isCurMove));
        } catch (IOException e) {
            log.error("Failed to send the start message on port - %d".formatted(socket.getPort()));
        }
    }

    public void updateGame(int painterId, PlayersMove move, List<Integer> cells, boolean isCurMove) {
        try {
            objectOutputStream.writeObject(new Update(painterId, move, cells, isCurMove));
        } catch (IOException e) {
            log.error("Failed to send update field on port - %d".formatted(socket.getPort()));
        }
    }

    public void finishGame(int painterId, PlayersMove move, List<Integer> cells, List<Integer> score, boolean isWinner) {
        try {
            objectOutputStream.writeObject(new Finish(painterId, isWinner, move, cells, score));
            objectOutputStream.reset();
        } catch (IOException e) {
            log.error("Failed to send finish result on port - %d".formatted(socket.getPort()));
        }
    }
}
