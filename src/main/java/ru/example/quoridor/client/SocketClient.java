package ru.example.quoridor.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.example.quoridor.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static ru.example.quoridor.property.Property.PORT;

public class SocketClient {

    private static final Logger log = LogManager.getLogger(SocketClient.class);

    private final ClientManager manager = BClientManager.getManager();
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public SocketClient() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Socket socket = new Socket(ip, PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            log.error("Failed to connect to the server");
            return;
        }
        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
        log.info("Client start");
    }

    void run() {
        while (true) {
            try {
                Object obj = ois.readObject();
                if (obj instanceof StartGameMsg msg) {
                    log.info("Start message");
                    manager.startGame(msg);
                }
                else if (obj instanceof UpdateGameStatusMsg msg) {
                    log.info("Update message");
                    manager.updateGameStatus(msg);
                }
                else if (obj instanceof FinishGameMsg msg) {
                    log.info("Finish message");
                    manager.finishGame(msg);
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

    public void sendReady() {
        try {
            oos.writeObject(SignalMsg.READY);
        }
        catch (IOException e) {
            log.error("Failed to send ready message");
        }
    }

    public void sendLine(int id, LineType type) {
        try {
            oos.writeObject(new PaintingLine(type, id));
        }
        catch (IOException e) {
            log.error("Failed to send line message");
        }
    }
}
