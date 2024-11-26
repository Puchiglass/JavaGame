package ru.example.quoridor.client;

public class BClientManager {

    static final ClientManager manager = new ClientManager();

    static ClientManager getManager() {
        return manager;
    }

}
