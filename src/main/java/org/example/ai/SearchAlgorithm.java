package org.example.ai;
import org.example.game.GameState;

public interface SearchAlgorithm<M> {

    /**
     * General interface for search algorithms.
     * @author Eli Boyden, eboyden42
     */

    /**
     * Finds the best move given a game state.
     * @param state the current game state
     * @return the best move for the current player
     */
    M findBestMove(GameState<M> state);
}
