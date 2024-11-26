package org.corridor_game.corridor_game.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.corridor_game.corridor_game.messages.*;

import java.util.ArrayList;

public class VisualController {
    ClientManager manager = BClientManager.getManager();
    static final int field_size = 3;
    static final int cell_pixel_size = 100;
    static final int start_field_x = 50;
    static final int start_field_y = 100;
    static final Color[] colors_for_players = {Color.BLUE, Color.RED};

    Line[] vertical_lines = new Line[field_size * (field_size + 1)];
    Line[] horizontal_lines = new Line[field_size * (field_size + 1)];
    Rectangle[] cells = new Rectangle[field_size * field_size];


    StartWindow start_window;
    FinishWindow finish_window;

    @FXML
    public void initialize() {
        manager.setController(this);
        Line line;
        for (int i = 0; i < vertical_lines.length; i++) {
            line = new Line(0, 0, cell_pixel_size, 0);
            line.setStrokeWidth(8);
            line.setId(String.valueOf(i));
            line.setStroke(Color.GREY);
            line.setOnMouseClicked(event -> sendLine(event, LineType.HORIZONTAL));
            line.setLayoutX(start_field_x + i % field_size * cell_pixel_size);
            line.setLayoutY(start_field_y + i / field_size * cell_pixel_size);
            main_pane.getChildren().add(line);
            horizontal_lines[i] = line;

            line = new Line(0, 0, 0, cell_pixel_size);
            line.setStrokeWidth(8);
            line.setId(String.valueOf(i));
            line.setStroke(Color.GREY);
            line.setOnMouseClicked(event -> sendLine(event, LineType.VERTICAL));
            line.setLayoutX(start_field_x + i % (field_size + 1) * cell_pixel_size);
            line.setLayoutY(start_field_y + i / (field_size + 1) * cell_pixel_size);
            main_pane.getChildren().add(line);
            vertical_lines[i] = line;
        }

        for (int i = 0; i < cells.length; i++) {
            Rectangle rec = new Rectangle(cell_pixel_size - 8, cell_pixel_size - 8);
            rec.setLayoutX(start_field_x + 4 + i % field_size * cell_pixel_size);
            rec.setLayoutY(start_field_y + 4 + (i / field_size) * cell_pixel_size);
            rec.setFill(Color.WHITE);
            rec.setOpacity(0.5);
            rec.setStrokeWidth(0);
            main_pane.getChildren().add(rec);
            cells[i] = rec;
        }

        cur_move_label = new Label();
        cur_move_label.setLayoutX(150);
        cur_move_label.setLayoutY(50);
        cur_move_label.setFont(Font.font(20));
        main_pane.getChildren().add(cur_move_label);

        stat_field.setSpacing(50);
        stat_field.setAlignment(Pos.TOP_CENTER);

        start_window = new StartWindow();
        start_window.show();
    }

    private void sendLine(MouseEvent event, LineType type) {
        Line line = (Line)event.getSource();
        if (line.getStroke() == Color.GREY) {
            int id = Integer.parseInt(line.getId());
            manager.sendLine(id, type);
        }
    }

    public void startGame(StartGameMsg msg) {
        Platform.runLater(()-> {
            setCurMove(msg.isCurMove());
            fillStatField(msg.getNumPlayers());
            if (finish_window != null) {
                reset();
                finish_window.close();
                finish_window = null;
            }
            else {
                start_window.close();
            }
        });
    }

    public void finishGame(FinishGameMsg msg) {
        Platform.runLater(() -> {
            updateField(msg.getPainterId(), msg.getLine(), msg.getCells());
            finish_window = new FinishWindow(msg.isWinner(), msg.getScore());
            finish_window.show();
        });
    }

    public void UpdateGameStatus(UpdateGameStatusMsg msg) {
        Platform.runLater(()-> {
            updateField(msg.getPainterId(), msg.getLine(), msg.getCells());
            setCurMove(msg.isCurMove());
        });
    }
    private void fillStatField(int num_players) {
        for (int i = stat_field.getChildren().size(); i < num_players; i++) {
            Label player_label = new Label("Player" + (i + 1));
            player_label.setFont(Font.font(30));
            player_label.setTextFill(colors_for_players[i]);
            stat_field.getChildren().add(player_label);
        }
    }
    private void setCurMove(boolean is_cur_move) {
        if (is_cur_move) {
            cur_move_label.setText("Твой Ход!");
        }
        else {
            cur_move_label.setText("Ход Противника!");
        }
    }

    private void updateField(int painter_id, PaintingLine line, ArrayList<Integer> colored_cells) {
        if (line.type == LineType.HORIZONTAL) {
            horizontal_lines[line.index].setStroke(colors_for_players[painter_id]);
            horizontal_lines[line.index].toFront();
        }
        else {
            vertical_lines[line.index].setStroke(colors_for_players[painter_id]);
            vertical_lines[line.index].toFront();
        }

        for (int cell_id : colored_cells) {
            cells[cell_id].setFill(colors_for_players[painter_id]);
        }
    }

    private void reset() {
        for (int i = 0; i < vertical_lines.length; i++) {
            vertical_lines[i].setStroke(Color.GREY);
            horizontal_lines[i].setStroke(Color.GREY);
        }
        for (Rectangle cell : cells) {
            cell.setFill(Color.WHITE);
        }
    }

    @FXML
    private Pane main_pane;
    @FXML
    private VBox stat_field;

    private Label cur_move_label;
}
