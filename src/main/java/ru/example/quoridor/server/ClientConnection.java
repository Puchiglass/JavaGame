package org.corridor_game.corridor_game.server;

import org.corridor_game.corridor_game.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    ServerManager manager;

    ClientConnection(Socket socket_, ServerManager manager_) {
        socket = socket_;
        manager = manager_;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            System.out.println("Error receiving streams! port - " + socket.getPort());
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

    void sendStartGame(boolean is_cur_move, int num_players) {
        try {
            oos.writeObject(new StartGameMsg(is_cur_move, num_players));
        }
        catch (IOException e) {
            System.out.println("Failed to send the start message! port - " + getPort());
        }
    }

    void sendUpdateGameStatus(int painter_id, PaintingLine line, ArrayList<Integer> cells, boolean is_cur_move) {
        try {
            oos.writeObject(new UpdateGameStatusMsg(painter_id, line, cells, is_cur_move));
        }
        catch (IOException e) {
            System.out.println("Failed to send update field! port - " + getPort());
        }
    }

    void sendFinishResult(int painter_id, PaintingLine line, ArrayList<Integer> cells,
                          ArrayList<Integer> score, boolean is_winner) {
        try {
            oos.writeObject(new FinishGameMsg(painter_id, line, cells, score, is_winner));
            oos.reset();
        }
        catch (IOException e) {
            System.out.println("Failed to send finish result! port - " + getPort());
        }
    }
}
