package ru.example.quoridor.client;

import ru.example.quoridor.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient {
    private final ClientManager manager = BClientManager.getManager();
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    SocketClient() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Socket socket = new Socket(ip, 5555);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Failed to connect to the server!");
            return;
        }
        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    void run() {
        while (true) {
            try {
                Object obj = ois.readObject();
                if (obj instanceof StartGameMsg msg) {
                    manager.startGame(msg);
                }
                else if (obj instanceof UpdateGameStatusMsg msg) {
                    manager.updateGameStatus(msg);
                }
                else if (obj instanceof FinishGameMsg msg) {
                    manager.finishGame(msg);
                }
            }
            catch (IOException e) {
                System.out.println("The received message could not be processed!");
            }
            catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void sendReady() {
        try {
            oos.writeObject(SignalMsg.READY);
        }
        catch (IOException e) {
            System.out.println("Failed to send ready message!");
        }
    }

    public void sendLine(int id, LineType type) {
        try {
            oos.writeObject(new PaintingLine(type, id));
        }
        catch (IOException e) {
            System.out.println("Failed to send line message!");
        }
    }
}
