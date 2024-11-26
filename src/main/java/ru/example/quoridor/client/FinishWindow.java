package org.corridor_game.corridor_game.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FinishWindow {
    ClientManager manager = BClientManager.getManager();
    Stage window;
    boolean is_winner;
    ArrayList<Integer> score;
    Label info;

    FinishWindow(boolean is_winner, ArrayList<Integer> score) {
        this.is_winner = is_winner;
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

        Label result_label = new Label();
        if (is_winner) {
            result_label.setText("Победа!!!");
        }
        else {
            result_label.setText("Поражение(");
        }
        result_label.setFont(Font.font(40));
        vBox.getChildren().add(result_label);

        for (int i = 0; i < score.size(); i++) {
            Label player_result = new Label("Player" + (i + 1) + " - " + score.get(i));
            player_result.setFont(Font.font(20));
            vBox.getChildren().add(player_result);
        }

        info = new Label("Нажмите \"Рестарт\", чтобы сыграть еще раз");
        vBox.getChildren().add(info);
        Button restart_game_btn = new Button("Рестарт");
        restart_game_btn.setOnAction(event-> restartGame());
        vBox.getChildren().add(restart_game_btn);

        window.showAndWait();
    }

    public void close() {
        window.close();
    }
    void restartGame() {
        manager.sendReady();
        info.setText("Ожидание второго игрока");
    }
}
