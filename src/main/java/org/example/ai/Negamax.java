package org.example.ai;

import org.example.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class Negamax<M> implements SearchAlgorithm<M> {
    /**
     * Implements SearchAlgorithm interface using the
     * <a href="https://en.wikipedia.org/wiki/Negamax">negamax algorithm</a>.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Indicates to what depth the algorithm will search.
     */
    private int depth;

    /**
     * Constructs a Negamax object initializing the depth.
     *
     * @param depth The depth for this instance of Negamax
     */
    public Negamax(int depth) {this.depth = depth;}

    /**
     * Finds the best move given a game state by running negamax on all possible moves from the given state.
     *
     * @param state the current game state
     * @return the best move for the current player
     */
    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = Integer.MIN_VALUE;
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = -negamax(state.applyMove(possibleMoves.get(i)), depth);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    /**
     * Implements <a href="https://en.wikipedia.org/wiki/Negamax">negamax</a> to evaluate the best move for the current player.
     *
     * @param node The current game state being evaluated.
     * @param depth The maximum depth to explore in the game tree.
     * @return The evaluation score for the current game state.
     */
    public int negamax(GameState<M> node, int depth) {
        if (depth == 0 || node.isTerminal()) {
            return node.evaluate(node.getCurrentPlayer()); // If the node is terminal, return static evaluation
        }
        int value = Integer.MIN_VALUE;
        List<M> possibleMoves = node.getPossibleMoves();
        for (int i = 0; i < possibleMoves.size(); i ++) {
            value = Math.max(value, -negamax(node.applyMove(possibleMoves.get(i)), depth-1));
        }
        return value;
    }
}
