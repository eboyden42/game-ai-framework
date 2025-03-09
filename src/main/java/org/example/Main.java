package org.example;

import org.example.ai.AlphaBeta;
import org.example.ai.IterativeDeepeningAlphaBeta;
import org.example.ai.Minimax;
import org.example.game.UltimateMove;
import org.example.game.Ultimate;

public class Main {
    public static void main(String[] args) {
        Play<UltimateMove> play = new Play<UltimateMove>(new Ultimate(), new IterativeDeepeningAlphaBeta<>(500));
        play.start();
    }
}