package org.example.ai;

import org.example.game.GameState;

import java.util.List;

public class PrincipalVariationSearch<M> implements SearchAlgorithm<M> {
/**
 * Implements SearchAlgorithm interface using a
 * <a href="https://en.wikipedia.org/wiki/Principal_variation_search">principal variation search</a> (PVS).
 *
 * @author Eli Boyden, eboyden42
 */

    /**
     * Indicates to what depth the PVS algorithm will search.
     */
    private int depth;

    /**
     * Constructs an object initializing the depth.
     *
     * @param depth The depth for this instance
     */
    public PrincipalVariationSearch(int depth) {
        this.depth = depth;
    }

    /**
     * Finds the best move given a game state by running PVS on all possible moves from the given state.
     *
     * @param state the current game state
     * @return the best move for the current player
     */
    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = Integer.MIN_VALUE;
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = pvs(state.applyMove(possibleMoves.get(i)), depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    /**
     * Implements PVS to evaluate the state of the board.
     *
     * @param node The current game state being evaluated.
     * @param depth The maximum depth to explore in the game tree.
     * @return The evaluation score for the current game state.
     */
    public int pvs(GameState<M> node, int depth, int alpha, int beta) {
        if (depth == 0 || node.isTerminal()) {
            return -node.evaluate(node.getCurrentPlayer()); // If the node is terminal, return static evaluation
        }
        List<M> possibleMoves = node.getPossibleMoves();
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int eval;
            if (i == 0) { // If first child, do a full window search
                eval = -pvs(node.applyMove(possibleMoves.get(i)), depth-1, -beta, -alpha);
            }
            else { // Otherwise, search with a null window
                eval = -pvs(node.applyMove(possibleMoves.get(i)), depth-1, -alpha-1, -alpha);
                if (alpha < eval && eval < beta) { // Eval between alpha and beta indicates a fail high, so do a re-search
                    eval = -pvs(node.applyMove(possibleMoves.get(i)), depth-1, -beta, -alpha);
                }
            }
            alpha = Math.max(alpha, eval);
            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }
}
