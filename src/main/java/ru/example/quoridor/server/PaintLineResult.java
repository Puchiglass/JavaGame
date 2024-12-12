package ru.example.quoridor.server;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PaintLineResult {

    public PaintLineResultType type;
    public List<Integer> colored_cells = new ArrayList<>();

    PaintLineResult(PaintLineResultType type_) {
        type = type_;
    }

}
