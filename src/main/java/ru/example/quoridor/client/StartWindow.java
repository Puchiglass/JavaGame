package org.corridor_game.corridor_game.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartWindow {
    ClientManager manager = BClientManager.getManager();
    Label info;
    Stage window;

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

        Button ready_btn = new Button("Готов");
        ready_btn.setOnAction(event-> readyAction());
        vBox.getChildren().add(ready_btn);

        window.showAndWait();
    }

    void readyAction() {
        info.setText("Ожидание готовности второго игрока");
        manager.sendReady();
    }

    public void close() {
        window.close();
    }
}
