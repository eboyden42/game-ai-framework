package org.example.ai;

import org.example.game.GameState;
import org.example.game.Ultimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Minimax<M> implements SearchAlgorithm<M> {
    /**
     * Implements SearchAlgorithm interface using the
     * <a href="https://en.wikipedia.org/wiki/Minimax">Minimax algorithm</a>.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Indicates to what depth the minimax algorithm will search.
     */
    private int depth;

    /**
     * Constructs a Minimax object initializing the depth.
     *
     * @param depth The depth for this instance of Minimax
     */
    public Minimax(int depth) {
        this.depth = depth;
    }

    /**
     * Finds the best move given a game state by running minimax on all possible moves from the given state.
     *
     * @param state the current game state
     * @return the best move for the current player
     */
    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = Integer.MIN_VALUE;
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = minimax(state.applyMove(possibleMoves.get(i)), depth, false);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    /**
     * Implements the <a href="https://en.wikipedia.org/wiki/Minimax">Minimax algorithm</a> to evaluate the best move for the current player.
     *
     * @param node The current game state being evaluated.
     * @param depth The maximum depth to explore in the game tree.
     * @param isMaximizingPlayer {@code true} if the current player is the maximizing player, {@code false} if the
     * current player is the minimizing player.
     * @return The evaluation score for the current game state.
     */
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
