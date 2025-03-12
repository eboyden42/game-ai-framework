package org.example.ai;

import org.example.game.GameState;
import org.example.game.Ultimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Minimax<M> implements SearchAlgorithm<M> {

    private int depth;

    public Minimax(int depth) {
        this.depth = depth;
    }

    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = -1000000;
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = this.minimax(state.applyMove(possibleMoves.get(i)), depth, false);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }


    public int minimax(GameState<M> node, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || node.isTerminal()) {
            return node.evaluate(node.getCurrentPlayer()); // If the node is terminal, return static evaluation
        }
        if (isMaximizingPlayer) { // Maximizing player chooses the highest value
            int value = -100000;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.max(value, minimax(node.applyMove(possibleMoves.get(i)), depth-1, false));
            }
            return value;
        }
        else { // Minimizing player chooses the lowest value
            int value = 100000;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.min(value, minimax(node.applyMove(possibleMoves.get(i)), depth-1, true));
            }
            return value;
        }
    }
}
