package ru.example.quoridor.server;

import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class PaintLineResult {

    public PaintLineResultType type;
    public ArrayList<Integer> colored_cells = new ArrayList<>();

    PaintLineResult(PaintLineResultType type_) {
        type = type_;
    }

}
