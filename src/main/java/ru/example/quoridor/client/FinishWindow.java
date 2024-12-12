package ru.example.quoridor.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FinishWindow {

    private final ClientManager manager = BClientManager.getManager();
    private final boolean isWinner;
    private final List<Integer> score;

    private Stage window;
    private Label info;

    FinishWindow(boolean isWinner, List<Integer> score) {
        this.isWinner = isWinner;
        this.score = score;
    }

    public void show() {
        window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 350, 250);
        window.setScene(scene);
        window.setResizable(false);
        window.setTitle("Результат");

        Label resultLabel = new Label();
        if (isWinner) {
            resultLabel.setText("Победа!!!");
        }
        else {
            resultLabel.setText("Поражение(");
        }
        resultLabel.setFont(Font.font(40));
        vBox.getChildren().add(resultLabel);

        for (int i = 0; i < score.size(); i++) {
            Label playerResult = new Label("Player" + (i + 1) + " - " + score.get(i));
            playerResult.setFont(Font.font(20));
            vBox.getChildren().add(playerResult);
        }

        info = new Label("Нажмите \"Рестарт\", чтобы сыграть еще раз");
        vBox.getChildren().add(info);
        Button restartGameBtn = new Button("Рестарт");
        restartGameBtn.setOnAction(event-> restartGame());
        vBox.getChildren().add(restartGameBtn);

        window.showAndWait();
    }

    public void close() {
        window.close();
    }
    void restartGame() {
        manager.ready();
        info.setText("Ожидание второго игрока");
    }

}
