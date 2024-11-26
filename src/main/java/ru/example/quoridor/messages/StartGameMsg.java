package ru.example.quoridor.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class StartGameMsg implements Serializable {

    private boolean isCurMove;
    private int numPlayers;

}
