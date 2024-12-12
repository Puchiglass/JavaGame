package ru.example.quoridor.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.example.quoridor.messages.LineType;
import ru.example.quoridor.messages.PlayersMove;
import ru.example.quoridor.messages.Start;
import ru.example.quoridor.messages.Update;

import java.util.List;

public class CorridorGame extends Application {
    private static final int FIELD_SIZE = 3;
    private static final int CELL_PIXEL_SIZE = 100;
    private static final int START_FIELD_X = 50;
    private static final int START_FIELD_Y = 100;

    private final ClientManager manager = BClientManager.getManager();

    private final Line[] verticalLines = new Line[(FIELD_SIZE + 1) * FIELD_SIZE];
    private final Line[] horizontalLines = new Line[(FIELD_SIZE + 1) * FIELD_SIZE];
    private final Rectangle[] cells = new Rectangle[FIELD_SIZE * FIELD_SIZE];
    private final Text[] cellTexts = new Text[FIELD_SIZE * FIELD_SIZE];
    private boolean isPlayerMove = false;

    private final Pane root = new Pane();
    private Button startButton;
    private Label moveLabel;

    @Override
    public void start(Stage primaryStage) {
        initializeSocket();
        initializeLines();
        initializeCells();
        initializeMoveLabel();
        initializeStartButton();

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Коридорчики");
        primaryStage.show();
    }

    public void start(Start msg) {
        Platform.runLater(() -> {
            updateMoveLabel(msg.isCurMove());
        });
    }

    public void update(Update msg) {
        Platform.runLater(()-> {
            updateField(msg.painterId(), msg.move(), msg.cells());
            updateMoveLabel(msg.isCurMove());
        });
    }

    public void finish(Finish msg) {
        Platform.runLater(() -> {
            updateField(msg.painterId(), msg.move(), msg.cells());
            if (msg.isWinner()) {
                moveLabel.setText("Вы победили )");
            } else {
                moveLabel.setText("Вы проиграли (");
            }
        });
    }

    private void updateField(int painterId, PlayersMove move, List<Integer> coloredCells) {
        switch (move.type()) {
            case HORIZONTAL:
                horizontalLines[move.id()].setStroke(Color.BLACK);
                horizontalLines[move.id()].toFront();
                break;
            case VERTICAL:
                verticalLines[move.id()].setStroke(Color.BLACK);
                verticalLines[move.id()].toFront();
                break;
        }
        coloredCells.forEach(cell_id -> cellTexts[cell_id].setText(painterId == 0 ? "X" : "O"));
    }

    private void updateMoveLabel(boolean isCurMove) {
        isPlayerMove = isCurMove;
        if (isPlayerMove) {
            root.getChildren().remove(moveLabel);
            moveLabel.setText("Твой ход");
            root.getChildren().add(moveLabel);
        } else {
            root.getChildren().remove(moveLabel);
            moveLabel.setText("Ход противника");
            root.getChildren().add(moveLabel);
        }
    }

    private void initializeSocket() {
        SocketClient socket = new SocketClient();
        ClientManager manager = BClientManager.getManager();
        manager.setSocket(socket);
        manager.setCorridorGame(this);
    }

    private void initializeMoveLabel() {
        moveLabel = new Label();
        moveLabel.setLayoutX(150);
        moveLabel.setLayoutY(50);
        moveLabel.setFont(Font.font(20));
        root.getChildren().add(moveLabel);
    }

    private void initializeStartButton() {
        startButton = new Button("Начать игру");
        startButton.setFont(Font.font(15));
        startButton.setOnMouseClicked(event -> sendReady());
        startButton.setLayoutX(10);
        startButton.setLayoutY(50);
        root.getChildren().add(startButton);
    }

    private void initializeCells() {
        for (int i = 0; i < cells.length; i++) {
            Rectangle cell = new Rectangle();
            cell.setStrokeWidth(0);
            cell.setOpacity(0.5);
            cell.setFill(Color.WHITE);
            cell.setWidth(CELL_PIXEL_SIZE - 8);
            cell.setHeight(CELL_PIXEL_SIZE - 8);
            cell.setLayoutX(i % FIELD_SIZE * CELL_PIXEL_SIZE + START_FIELD_X + 4);
            cell.setLayoutY((i / FIELD_SIZE) * CELL_PIXEL_SIZE + START_FIELD_Y + 4);

            cells[i] = cell;
            root.getChildren().add(cell);

            Text cellText = new Text();
            cellText.setFont(Font.font(30));
            cellText.setX(cell.getLayoutX() + CELL_PIXEL_SIZE / 2 - 10);
            cellText.setY(cell.getLayoutY() + CELL_PIXEL_SIZE / 2 + 10);
            cellTexts[i] = cellText;
            root.getChildren().add(cellText);
        }
    }

    private void initializeLines() {
        for (int i = 0; i < verticalLines.length; i++) {
            Line horizontalLine = createLine(CELL_PIXEL_SIZE, 0, i, LineType.HORIZONTAL);
            horizontalLines[i] = horizontalLine;
            root.getChildren().add(horizontalLine);

            Line verticalLine = createLine(0, CELL_PIXEL_SIZE, i, LineType.VERTICAL);
            verticalLines[i] = verticalLine;
            root.getChildren().add(verticalLine);
        }
    }

    private void sendReady() {
        manager.ready();
        Platform.runLater(() -> {
            moveLabel.setText("Ожидание готовности второго игрока...");
            root.getChildren().remove(startButton);
        });

    }

    private Line createLine(double endX, double endY, int id, LineType type) {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setOnMouseClicked(event -> updateLine(type, event));
        line.setId(String.valueOf(id));
        line.setStroke(Color.LIGHTGRAY);
        line.setStrokeWidth(8);
        switch (type) {
            case HORIZONTAL:
                line.setLayoutY(id / FIELD_SIZE * CELL_PIXEL_SIZE + START_FIELD_Y);
                line.setLayoutX(id % FIELD_SIZE * CELL_PIXEL_SIZE + START_FIELD_X);
                break;
            case VERTICAL:
                line.setLayoutY(id / (FIELD_SIZE + 1) * CELL_PIXEL_SIZE + START_FIELD_Y);
                line.setLayoutX(id % (FIELD_SIZE + 1) * CELL_PIXEL_SIZE+ START_FIELD_X);
                break;
        }
        return line;
    }

    private void updateLine(LineType type, MouseEvent event) {
        Line line = (Line) event.getSource();
        if (line.getStroke() != Color.LIGHTGRAY || !isPlayerMove) {
            return;
        }
        manager.sendLine(Integer.parseInt(line.getId()), type);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
