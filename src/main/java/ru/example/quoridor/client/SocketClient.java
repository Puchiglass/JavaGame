package ru.example.quoridor.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.example.quoridor.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static ru.example.quoridor.property.Property.PORT;

public class SocketClient {

    private static final Logger log = LogManager.getLogger(SocketClient.class);

    private final ClientManager manager = ClientManager.getInstance();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;

    public SocketClient() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(this::run);
            thread.setDaemon(true);
            thread.start();
            log.info("Client started");
        } catch (IOException e) {
            log.error("Failed to connect to the server", e);
            closeResources();
        }
    }

    public void sendLine(int id, LineType type) {
        try {
            objectOutputStream.writeObject(new PlayersMove(type, id));
        }
        catch (IOException e) {
            log.error("Failed to send line message");
        }
    }

    public void sendReady(Ready ready) {
        try {
            objectOutputStream.writeObject(ready);
        } catch (IOException e) {
            log.error("Failed to send ready message", e);
        }
    }

    private void run() {
        while (true) {
            try {
                Object obj = objectInputStream.readObject();
                if (obj instanceof Start msg) {
                    System.out.println("Start message");
                    manager.startGame(msg);
                }
                if (obj instanceof Update msg) {
                    System.out.println("Update message");
                    manager.updateGame(msg);
                }
                if (obj instanceof Finish finish) {
                    System.out.println("Finish message");
                    manager.finishGame(finish);
                }
            }
            catch (IOException e) {
                log.error("Undefined message");
            }
            catch (ClassNotFoundException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void closeResources() {
        try {
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            log.error("Failed to close resources", e);
        }
    }
}
