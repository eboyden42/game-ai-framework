package org.example;
import org.example.game.*;
import org.example.ai.*;

import java.util.Optional;
import java.util.Scanner;

public class Play<M> {
    /**
     * This class acts as a framework to play games implementing GameState<M> against a computer player implementing
     * SearchAlgorithm<M>.
     * In this implementation, player 1 is human and other players are AI.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Class for a two-player game that implements GameState.
     */
    private GameState<M> gameState;

    /**
     * Class for a search algorithm that implements SearchAlgorithm.
     */
    private SearchAlgorithm<M> cpuAlgorithm;

    /**
     * Constructs a Play object that stores the game being played and the algorithm being used.
     *
     * @param gameState Object that represents the game being played
     * @param cpuAlgorithm Object that represents the algorithm being used
     */
    public Play(GameState<M> gameState, SearchAlgorithm<M> cpuAlgorithm) {
        this.gameState = gameState;
        this.cpuAlgorithm = cpuAlgorithm;
    }

    /**
     * Plays the given game against the given AI with the human going first.
     */
    public void start() {
        System.out.println("Starting Game!");
        gameState.print();

        while (!gameState.isTerminal()) {
            if (gameState.getCurrentPlayer() == 1) {
                humanMove();
            } else {
                cpuMove();
            }
            gameState.print();
        }

        announceResult();
    }

    /**
     * Gets user input and applies that move to the gameState.
     */
    private void humanMove() {
        Optional<M> humanMove = gameState.getPlayerInputMove();
        while (humanMove.isEmpty()) {
            humanMove = gameState.getPlayerInputMove();
        }

        gameState = gameState.applyMove(humanMove.get());
    }

    /**
     * Uses the given SearchAlgorithm to get a CPU move and apply it to the gameState.
     */
    private void cpuMove() {
        System.out.println("CPU is thinking...");
        M bestMove = cpuAlgorithm.findBestMove(gameState); // Use given SearchAlgorithm to find the best move
        System.out.println("CPU chooses move: " + bestMove);
        gameState = gameState.applyMove(bestMove);
    }

    /**
     * Generates print statements based on a terminal state of the game.
     */
    private void announceResult() {
        int winner = gameState.evaluateWinner();
        if (winner == 1) {
            System.out.println("Congratulations! You win!");
        } else if (winner == 0) {
            System.out.println("It's a draw!");
        } else {
            System.out.printf("CPU (player %d) wins! Better luck next time.\n", winner);
        }
    }
}

