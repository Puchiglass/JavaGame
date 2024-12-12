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
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;

    public SocketClient() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            socket = new Socket(ip, PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.error("Failed to connect to the server", e);
            closeResources();
            return;
        }
        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
        log.info("Client started");
    }

    public void sendReady(Ready ready) {
        try {
            oos.writeObject(ready);
        } catch (IOException e) {
            log.error("Failed to send ready message", e);
        }
    }

    public void sendLine(int id, LineType type) {
        try {
            oos.writeObject(new PlayersMove(type, id));
        }
        catch (IOException e) {
            log.error("Failed to send line message");
        }
    }

    private void run() {
        while (true) {
            try {
                Object obj = ois.readObject();
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
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            log.error("Failed to close resources", e);
        }
    }
}
