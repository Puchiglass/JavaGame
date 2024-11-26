package ru.example.quoridor.messages;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class PaintingLine implements Serializable {

    public LineType type;
    public int index;

}
