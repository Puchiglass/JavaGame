package ru.example.quoridor.server;

import ru.example.quoridor.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection {
    private final Socket socket;
    private final ServerManager manager;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientConnection(Socket socket, ServerManager manager) {
        this.socket = socket;
        this.manager = manager;
        try {
            oos = new ObjectOutputStream(this.socket.getOutputStream());
            ois = new ObjectInputStream(this.socket.getInputStream());
        }
        catch (IOException e) {
            System.out.println("Error receiving streams! port - " + this.socket.getPort());
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
                if (obj instanceof SignalMsg msg) {
                    if (msg == SignalMsg.READY) {
                        manager.processReadyMsg(getPort());
                    }
                }
                else if (obj instanceof PaintingLine line) {
                    manager.processPaintLineMsg(getPort(), line);
                }
            }
            catch (IOException e) {
                System.out.println("Client disconnect!");
                break;
            }
            catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void sendStartGame(boolean isCurMove, int numPlayers) {
        try {
            oos.writeObject(new StartGameMsg(isCurMove, numPlayers));
        }
        catch (IOException e) {
            System.out.println("Failed to send the start message! port - " + getPort());
        }
    }

    void sendUpdateGameStatus(int painterId, PaintingLine line, ArrayList<Integer> cells, boolean isCurMove) {
        try {
            oos.writeObject(new UpdateGameStatusMsg(painterId, line, cells, isCurMove));
        }
        catch (IOException e) {
            System.out.println("Failed to send update field! port - " + getPort());
        }
    }

    void sendFinishResult(int painterId, PaintingLine line, ArrayList<Integer> cells,
                          ArrayList<Integer> score, boolean isWinner) {
        try {
            oos.writeObject(new FinishGameMsg(painterId, isWinner, line, cells, score));
            oos.reset();
        }
        catch (IOException e) {
            System.out.println("Failed to send finish result! port - " + getPort());
        }
    }
}
