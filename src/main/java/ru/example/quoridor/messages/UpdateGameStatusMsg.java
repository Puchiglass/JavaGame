package ru.example.quoridor.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class UpdateGameStatusMsg implements Serializable {

    private int painterId;
    private PaintingLine line;
    private ArrayList<Integer> cells;
    private boolean isCurMove;

}
