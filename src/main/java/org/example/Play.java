package org.example;
import org.example.game.*;
import org.example.ai.*;

import java.util.Optional;
import java.util.Scanner;

public class Play<M> {
    public static final int DEPTH = 9;
    private GameState<M> gameState;
    private SearchAlgorithm<M> cpuAlgorithm;
    private Scanner scanner;

    public Play(GameState<M> gameState, SearchAlgorithm<M> cpuAlgorithm) {
        this.gameState = gameState;
        this.cpuAlgorithm = cpuAlgorithm;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Game Start!");
        gameState.print(); // Assume the GameState interface has a print method

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

    private void humanMove() {
        Optional<M> humanMove = gameState.getPlayerInputMove();
        while (humanMove.isEmpty()) {
            humanMove = gameState.getPlayerInputMove();
        }

        gameState = gameState.applyMove(humanMove.get());
    }

    private void cpuMove() {
        System.out.println("CPU is thinking...");
        M bestMove = cpuAlgorithm.findBestMove(gameState, DEPTH); // Search with depth 5
        System.out.println("CPU chooses move: " + bestMove);
        gameState = gameState.applyMove(bestMove);
    }

    private void announceResult() {
        int winner = gameState.evaluateWinner(); // Assume this method returns the winner
        if (winner == 1) {
            System.out.println("Congratulations! You win!");
        } else if (winner == 0) {
            System.out.println("It's a draw!");
        } else {
            System.out.printf("CPU (player %d) wins! Better luck next time.\n", winner);
        }
    }
}

