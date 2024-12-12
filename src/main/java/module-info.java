module ru.example.quoridor.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.apache.logging.log4j;


    opens ru.example.quoridor.client to javafx.fxml;
    exports ru.example.quoridor.client;
    exports ru.example.quoridor.model;
    opens ru.example.quoridor.model to javafx.fxml;
}