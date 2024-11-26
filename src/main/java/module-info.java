module ru.example.quoridor.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens ru.example.quoridor.client to javafx.fxml;
    exports ru.example.quoridor.client;
    exports ru.example.quoridor.messages;
}