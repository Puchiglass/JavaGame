package org.corridor_game.corridor_game.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    ServerManager manager;
    int port = 5555;
    SocketServer(ServerManager manager_) {
        manager = manager_;
    }

    void waitNewPlayers() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ServerSocket ss = new ServerSocket(port, 0, ip);
            Socket cs;
            while (true) {
                cs = ss.accept();
                if (manager.getPlayers().size() == 2) {
                    cs.close();
                    continue;
                }
                System.out.println("New client! port - " + cs.getPort());
                ClientConnection cc = new ClientConnection(cs, manager);
                manager.addNewPlayer(cc);
            }
        }
        catch (IOException ex) {
            System.out.println("Server startup error!");
        }
    }
}
