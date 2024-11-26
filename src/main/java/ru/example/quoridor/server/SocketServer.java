package ru.example.quoridor.server;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SocketServer {

    private final ServerManager manager;

    public void waitNewPlayers() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ServerSocket ss = new ServerSocket(5555, 0, ip);
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
