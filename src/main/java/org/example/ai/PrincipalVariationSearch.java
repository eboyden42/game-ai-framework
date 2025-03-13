package org.example.ai;

import org.example.game.GameState;

import java.util.List;

public class PrincipalVariationSearch<M> implements SearchAlgorithm<M> {
/**
 * Implements SearchAlgorithm interface using a
 * <a href="https://en.wikipedia.org/wiki/Principal_variation_search">principal variation search</a> (aka negascout).
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
     * Finds the best move given a game state by running minimax on all possible moves from the given state.
     *
     * @param state the current game state
     * @return the best move for the current player
     */
    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = -1000000;
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = pvs(state.applyMove(possibleMoves.get(i)), depth, false);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }
}
