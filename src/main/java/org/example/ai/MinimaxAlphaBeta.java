package org.example.ai;

import org.example.game.GameState;

import java.util.List;

public class MinimaxAlphaBeta<M> implements SearchAlgorithm<M> {
    /**
     * Implements SearchAlgorithm interface using minimax with
     * <a href="https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning">alpha-beta pruning</a>.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Indicates to what depth the algorithm will search.
     */
    private int depth;

    /**
     * The player to be used for perspective when performing static evaluations.
     */
    private int rootPlayer;

    /**
     * Constructs an AlphaBeta object initializing the depth.
     *
     * @param depth The depth for this instance of MinimaxAlphaBeta
     */
    public MinimaxAlphaBeta(int depth) {
        this.depth = depth;
    }

    /**
     * Set the root player for testing.
     * @param player the player to set the rootPlayer as.
     */
    public void setRootPlayer(int player) {
        rootPlayer = player;
    }

    /**
     * Finds the best move given a game state by running alpha-beta on all possible moves from the given state.
     *
     * @param state the current game state
     * @return the best move for the current player
     */
    public M findBestMove(GameState<M> state) {
        List<M> possibleMoves = state.getPossibleMoves();
        int highestIndex = 0;
        int highestScore = Integer.MIN_VALUE;
        rootPlayer = state.getCurrentPlayer();
        for (int i = 0; i < possibleMoves.size(); i ++) {
            int score = this.alphabeta(state.applyMove(possibleMoves.get(i)), depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        return possibleMoves.get(highestIndex);
    }

    /**
     * Implements <a href="https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning">alpha-beta pruning</a> to evaluate the best move for the current player.
     *
     * @param node The current game state being evaluated.
     * @param depth The maximum depth to explore in the game tree.
     * @param alpha The best value that the maximizing player can guarantee so far.
     * @param beta The best value that the minimizing player can guarantee so far.
     * @return The evaluation score for the current game state.
     */
    public int alphabeta(GameState<M> node, int depth, int alpha, int beta) {
        if (depth == 0 || node.isTerminal()) {
            return node.evaluate(rootPlayer); // If the node is terminal, return static evaluation
        }
        if (rootPlayer == node.getCurrentPlayer()) { // Maximizing player chooses the highest value
            int value = Integer.MIN_VALUE;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.max(value, alphabeta(node.applyMove(possibleMoves.get(i)), depth-1, alpha, beta));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        }
        else { // Minimizing player chooses the lowest value
            int value = Integer.MAX_VALUE;
            List<M> possibleMoves = node.getPossibleMoves();
            for (int i = 0; i < possibleMoves.size(); i ++) {
                value = Math.min(value, alphabeta(node.applyMove(possibleMoves.get(i)), depth-1, alpha, beta));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }
}
