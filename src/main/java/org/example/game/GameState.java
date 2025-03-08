package org.example.game;
import java.util.*;

public interface GameState<M> {
    /**
     * This interface must be implemented to run game search algorithms.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Gets the list of all possible moves from this state.
     * @return a list of valid moves.
     */
    List<M> getPossibleMoves();

    /**
     * Applies a move to get a new game state.
     * @param move the move to apply.
     * @return the new game state after applying the move.
     */
    GameState<M> applyMove(M move);

    /**
     * Checks if the game state is terminal (game over).
     * @return true if the game is over, false otherwise.
     */
    boolean isTerminal();

    /**
     * Evaluates the current state from the perspective of a given player.
     * @param player the player whose perspective is considered.
     * @return an evaluation score (higher is better for the player).
     */
    int evaluate(int player);

    /**
     * Gets the player whose turn it is in this state.
     * @return the ID of the current player.
     */
    int getCurrentPlayer();
}
