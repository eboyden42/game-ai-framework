package org.example.game;
import java.util.*;

public interface GameState<M> {
    /**
     * This interface must be implemented to run game search algorithms and to use the Play class.
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

    /**
     * Prints the state of the game to the terminal
     */
    void printState();

    /**
     * Returns the player number of the winner. If there is no winner returns 0.
     * @return winning player number.
     */
    int evaluateWinner();

    /**
     * Gets player input and converts it to a move. If the input of the player is not valid move, return an empty optional.
     * An invalid move in this case is simply input that cannot be parsed into a type M object, and moves that violate the game rules
     * at the current state of the game (using getPossibleMoves() can be useful).
     *
     * Getting user input should be user-friendly with descriptive yet concise directions.
     *
     * @return Optional<M> type based on user input
     */
    Optional<M> getPlayerInputMove();

}
