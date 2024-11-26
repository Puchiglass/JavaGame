package org.corridor_game.corridor_game.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SocketClient socket = new SocketClient();
        ClientManager manager = BClientManager.getManager();
        manager.setSocket(socket);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        stage.setTitle("Коридорчики");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
