package org.example.ai;

import org.example.game.GameState;

import java.util.List;

public class IterativeDeepeningAlphaBeta<M> implements SearchAlgorithm<M> {
    private final long timeLimitMillis; // Time limit for search in milliseconds

    public IterativeDeepeningAlphaBeta(long timeLimitMillis) {
        this.timeLimitMillis = timeLimitMillis;
    }

    @Override
    public M findBestMove(GameState<M> state, int maxDepth) {
        M bestMove = null;
        long startTime = System.currentTimeMillis();
        int depth;
        for (depth = 1; depth <= maxDepth; depth++) {
            if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
                break; // Stop if time limit is reached
            }

            bestMove = depthLimitedAlphaBeta(state, depth, startTime);
        }
        System.out.printf("Max Depth Reached: %d\n", depth);
        return bestMove; // Return the best move found in time
    }

    private M depthLimitedAlphaBeta(GameState<M> state, int depth, long startTime) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = -1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < possibleMoves.size(); i ++) {
            if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
                break; // Stop if time limit is reached
            }
            int score = this.alphabeta(state.applyMove(possibleMoves.get(i)), depth, -100000, 100000, false, startTime);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    private int alphabeta(GameState<M> node, int depth, int alpha, int beta, boolean isMaximizing, long startTime) {
        if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
            return 0; // Return neutral value if time runs out
        }
        if (depth == 0 || node.isTerminal()) {
            return node.evaluate(node.getCurrentPlayer());
        }
        if (isMaximizing) {
            int value = -100000;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.max(value, alphabeta(node.applyMove(possibleMoves.get(i)), depth-1, alpha, beta, false, startTime));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        }
        else {
            int value = 100000;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.min(value, alphabeta(node.applyMove(possibleMoves.get(i)), depth-1, alpha, beta, true, startTime));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }
}
