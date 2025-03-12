package org.example.ai;

import org.example.game.GameState;
import org.example.game.Ultimate;

import java.util.ArrayList;
import java.util.List;

public class AlphaBeta<M> implements SearchAlgorithm<M> {

    private int depth;

    public AlphaBeta(int depth) {
        this.depth = depth;
    }

    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = -1000000;
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = this.alphabeta(state.applyMove(possibleMoves.get(i)), depth, -100000, 100000, false);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    public int alphabeta(GameState<M> node, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || node.isTerminal()) {
            return node.evaluate(node.getCurrentPlayer()); // If the node is terminal, return static evaluation
        }
        if (isMaximizingPlayer) { // Maximizing player chooses the highest value
            int value = -100000;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.max(value, alphabeta(node.applyMove(possibleMoves.get(i)), depth-1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        }
        else { // Minimizing player chooses the lowest value
            int value = 100000;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.min(value, alphabeta(node.applyMove(possibleMoves.get(i)), depth-1, alpha, beta, true));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }
}
