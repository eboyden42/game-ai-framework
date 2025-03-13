package org.example.ai;

import org.example.game.GameState;

import java.util.List;

public class IterativeDeepeningMinimaxAlphaBeta<M> implements SearchAlgorithm<M> {
    /**
     * Implements SearchAlgorithm interface using alpha-beta minimax with
     * <a href="https://www.chessprogramming.org/Iterative_Deepening">iterative deepening</a>.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Time limit for search in milliseconds.
     */
    private final long timeLimitMillis;

    /**
     * Constructs an IterativeDeepeningAlphaBeta object with a specified time limit.
     *
     * @param timeLimitMillis The time limit in milliseconds for the iterative deepening search.
     */
    public IterativeDeepeningMinimaxAlphaBeta(long timeLimitMillis) {
        this.timeLimitMillis = timeLimitMillis;
    }

    /**
     * Finds the best move given a game state by running iterative deepening alpha-beta on all possible moves from
     * the given state.
     *
     * @param state the current game state
     * @return the best move for the current player
     */
    @Override
    public M findBestMove(GameState<M> state) {
        M bestMove = null;
        long startTime = System.currentTimeMillis();
        int depth = 1;
        while (System.currentTimeMillis() - startTime < timeLimitMillis) {
            if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
                break; // Stop if the time limit is reached
            }

            bestMove = depthLimitedAlphaBeta(state, depth, startTime);
            depth ++;
        }
        System.out.printf("Max Depth Reached: %d\n", depth);
        return bestMove; // Return the best move found in time
    }

    /**
     * Runs alpha-beta limited by a depth and a time limit.
     *
     * @param node The current game state being evaluated.
     * @param depth The maximum depth to explore in the game tree.
     * @param startTime The time in millis that the program started at.
     * @return The evaluation score for the current game state.
     */
    private M depthLimitedAlphaBeta(GameState<M> node, int depth, long startTime) {
        List<M> possibleMoves = node.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = Integer.MIN_VALUE;
        long start = System.currentTimeMillis();
        for (int i = 0; i < possibleMoves.size(); i ++) {
            if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
                break; // Stop if the time limit is reached
            }
            int score = this.alphabeta(node.applyMove(possibleMoves.get(i)), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false, startTime);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    /**
     * Implements <a href="https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning">alpha-beta pruning</a> to evaluate
     * the best move for the current player with a limit on the amount of time the search can take.
     *
     * @param node The current game state being evaluated.
     * @param depth The maximum depth to explore in the game tree.
     * @param alpha The best value that the maximizing player can guarantee so far.
     * @param beta The best value that the minimizing player can guarantee so far.
     * @param isMaximizingPlayer {@code true} if the current player is the maximizing player, {@code false} if the
     * current player is the minimizing player.
     * @return The evaluation score for the current game state.
     */
    protected int alphabeta(GameState<M> node, int depth, int alpha, int beta, boolean isMaximizingPlayer, long startTime) {
        if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
            return 0; // Return neutral value if time runs out
        }
        if (depth == 0 || node.isTerminal()) {
            return node.evaluate(node.getCurrentPlayer());
        }
        if (isMaximizingPlayer) {
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
