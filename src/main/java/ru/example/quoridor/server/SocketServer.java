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

    private final ServerManager manager;

    public void waitNewPlayers() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ServerSocket ss = new ServerSocket(PORT, 0, ip);
            Socket cs;
            while (true) {
                cs = ss.accept();
                if (manager.getPlayers().size() == 2) {
                    cs.close();
                    continue;
                }
                log.info("New client on port %d".formatted(cs.getPort()));
                ClientConnection cc = new ClientConnection(cs, manager);
                manager.addNewPlayer(cc);
            }
        }
        catch (IOException ex) {
            log.error("Server start error");
        }
    }
}
