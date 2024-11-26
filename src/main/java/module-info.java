module org.corridor_game.corridor_game.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.corridor_game.corridor_game.client to javafx.fxml;
    exports org.corridor_game.corridor_game.client;
    exports org.corridor_game.corridor_game.messages;
}