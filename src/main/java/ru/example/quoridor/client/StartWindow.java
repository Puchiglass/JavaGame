package ru.example.quoridor.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartWindow {
    private final ClientManager manager = BClientManager.getManager();
    private Label info;
    private Stage window;

    public void show() {
        window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 250, 150);
        window.setScene(scene);
        window.setResizable(false);
        window.setTitle("Коридорчики");

        info = new Label("Нажмите \"Готов\" для начала игры");
        vBox.getChildren().add(info);

        Button readyBtn = new Button("Готов");
        readyBtn.setOnAction(event-> readyAction());
        vBox.getChildren().add(readyBtn);

        window.showAndWait();
    }

    void readyAction() {
        info.setText("Ожидание готовности второго игрока");
        manager.ready();
    }

    public void close() {
        window.close();
    }
}
