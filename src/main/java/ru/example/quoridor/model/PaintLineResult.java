package ru.example.quoridor.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PaintLineResult {

    public PaintLineResultType type;
    public List<Integer> markedCells = new ArrayList<>();

    public PaintLineResult(PaintLineResultType type) {
        this.type = type;
    }

}
