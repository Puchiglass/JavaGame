package ru.example.quoridor.server;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static ru.example.quoridor.property.Property.PORT;

@AllArgsConstructor
public class SocketServer {

    private static final Logger log = LogManager.getLogger(SocketServer.class);

    private final GameManager manager;

    public void waitNewPlayers() {
        try {
            ServerSocket ss = new ServerSocket(PORT, 0, InetAddress.getLocalHost());
            System.out.printf("Server started on port %d%n", PORT);
            while (true) {
                Socket cs = ss.accept();
                if (!manager.canBeAddedPlayer()) {
                    cs.close();
                    continue;
                }
                System.out.printf("New client on port %d%n", cs.getPort());
                manager.addNewPlayer(new ClientConnection(cs, manager));
            }
        } catch (IOException ex) {
            log.error("Server start error");
        }
    }
}
