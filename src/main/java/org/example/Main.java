package org.example;

import org.example.ai.Negamax;
import org.example.ai.NegamaxAlphaBeta;
import org.example.game.UltimateMove;
import org.example.game.Ultimate;

public class Main {
    public static void main(String[] args) {
        Play<UltimateMove> play = new Play<UltimateMove>(new Ultimate(), new NegamaxAlphaBeta<>(5));
        play.start();
    }
}