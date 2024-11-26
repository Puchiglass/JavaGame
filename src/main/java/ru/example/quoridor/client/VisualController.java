package ru.example.quoridor.client;

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
import ru.example.quoridor.messages.*;

import java.util.ArrayList;

public class VisualController {
    private static final int FIELD_SIZE = 3;
    private static final int CELL_PIXEL_SIZE = 100;
    private static final int START_FIELD_X = 50;
    private static final int START_FIELD_Y = 100;
    private static final Color[] COLORS_FOR_PLAYERS = {Color.BLUE, Color.RED};

    private final ClientManager manager = BClientManager.getManager();
    private final Line[] verticalLines = new Line[FIELD_SIZE * (FIELD_SIZE + 1)];
    private final Line[] horizontalLines = new Line[FIELD_SIZE * (FIELD_SIZE + 1)];
    private final Rectangle[] cells = new Rectangle[FIELD_SIZE * FIELD_SIZE];

    private StartWindow startWindow;
    private FinishWindow finishWindow;

    @FXML
    private Pane mainPane;
    @FXML
    private VBox statField;
    private Label curMoveLabel;

    @FXML
    public void initialize() {
        manager.setController(this);
        Line line;
        for (int i = 0; i < verticalLines.length; i++) {
            line = new Line(0, 0, CELL_PIXEL_SIZE, 0);
            line.setStrokeWidth(8);
            line.setId(String.valueOf(i));
            line.setStroke(Color.GREY);
            line.setOnMouseClicked(event -> sendLine(event, LineType.HORIZONTAL));
            line.setLayoutX(START_FIELD_X + i % FIELD_SIZE * CELL_PIXEL_SIZE);
            line.setLayoutY(START_FIELD_Y + i / FIELD_SIZE * CELL_PIXEL_SIZE);
            mainPane.getChildren().add(line);
            horizontalLines[i] = line;

            line = new Line(0, 0, 0, CELL_PIXEL_SIZE);
            line.setStrokeWidth(8);
            line.setId(String.valueOf(i));
            line.setStroke(Color.GREY);
            line.setOnMouseClicked(event -> sendLine(event, LineType.VERTICAL));
            line.setLayoutX(START_FIELD_X + i % (FIELD_SIZE + 1) * CELL_PIXEL_SIZE);
            line.setLayoutY(START_FIELD_Y + i / (FIELD_SIZE + 1) * CELL_PIXEL_SIZE);
            mainPane.getChildren().add(line);
            verticalLines[i] = line;
        }

        for (int i = 0; i < cells.length; i++) {
            Rectangle rec = new Rectangle(CELL_PIXEL_SIZE - 8, CELL_PIXEL_SIZE - 8);
            rec.setLayoutX(START_FIELD_X + 4 + i % FIELD_SIZE * CELL_PIXEL_SIZE);
            rec.setLayoutY(START_FIELD_Y + 4 + (i / FIELD_SIZE) * CELL_PIXEL_SIZE);
            rec.setFill(Color.WHITE);
            rec.setOpacity(0.5);
            rec.setStrokeWidth(0);
            mainPane.getChildren().add(rec);
            cells[i] = rec;
        }

        curMoveLabel = new Label();
        curMoveLabel.setLayoutX(150);
        curMoveLabel.setLayoutY(50);
        curMoveLabel.setFont(Font.font(20));
        mainPane.getChildren().add(curMoveLabel);

        statField.setSpacing(50);
        statField.setAlignment(Pos.TOP_CENTER);

        startWindow = new StartWindow();
        startWindow.show();
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
            if (finishWindow != null) {
                reset();
                finishWindow.close();
                finishWindow = null;
            }
            else {
                startWindow.close();
            }
        });
    }

    public void finishGame(FinishGameMsg msg) {
        Platform.runLater(() -> {
            updateField(msg.getPainterId(), msg.getLine(), msg.getCells());
            finishWindow = new FinishWindow(msg.isWinner(), msg.getScore());
            finishWindow.show();
        });
    }
    public void updateGameStatus(UpdateGameStatusMsg msg) {
        Platform.runLater(()-> {
            updateField(msg.getPainterId(), msg.getLine(), msg.getCells());
            setCurMove(msg.isCurMove());
        });
    }
    private void fillStatField(int numPlayers) {
        for (int i = statField.getChildren().size(); i < numPlayers; i++) {
            Label player_label = new Label("Player" + (i + 1));
            player_label.setFont(Font.font(30));
            player_label.setTextFill(COLORS_FOR_PLAYERS[i]);
            statField.getChildren().add(player_label);
        }
    }

    private void setCurMove(boolean isCurMove) {
        if (isCurMove) {
            curMoveLabel.setText("Твой Ход!");
        }
        else {
            curMoveLabel.setText("Ход Противника!");
        }
    }

    private void updateField(int painterId, PaintingLine line, ArrayList<Integer> coloredCells) {
        if (line.type == LineType.HORIZONTAL) {
            horizontalLines[line.index].setStroke(COLORS_FOR_PLAYERS[painterId]);
            horizontalLines[line.index].toFront();
        }
        else {
            verticalLines[line.index].setStroke(COLORS_FOR_PLAYERS[painterId]);
            verticalLines[line.index].toFront();
        }

        for (int cellId : coloredCells) {
            cells[cellId].setFill(COLORS_FOR_PLAYERS[painterId]);
        }
    }

    private void reset() {
        for (int i = 0; i < verticalLines.length; i++) {
            verticalLines[i].setStroke(Color.GREY);
            horizontalLines[i].setStroke(Color.GREY);
        }
        for (Rectangle cell : cells) {
            cell.setFill(Color.WHITE);
        }
    }
}
