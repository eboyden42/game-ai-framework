package org.example;
import org.example.game.*;
import org.example.ai.*;
import java.util.Scanner;

public class Play<M> {
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
        gameState.printState(); // Assume the GameState interface has a print method

        while (!gameState.isTerminal()) {
            if (gameState.getCurrentPlayer() == 1) {
                humanMove();
            } else {
                cpuMove();
            }
            gameState.printState();
        }

        announceResult();
    }

    private void humanMove() {
        System.out.println("Your turn! Available moves: " + gameState.getPossibleMoves());
        System.out.print("Enter your move: ");

        M move = null;
        while (move == null) {
            try {
                String input = scanner.nextLine();
                move = parseMove(input);
                if (!gameState.getPossibleMoves().contains(move)) {
                    throw new IllegalArgumentException("Invalid move! Try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                move = null;
            }
        }

        gameState = gameState.applyMove(move);
    }

    private void cpuMove() {
        System.out.println("CPU is thinking...");
        M bestMove = cpuAlgorithm.findBestMove(gameState, 5); // Search with depth 5
        System.out.println("CPU chooses move: " + bestMove);
        gameState = gameState.applyMove(bestMove);
    }

    private void announceResult() {
        int winner = gameState.evaluateWinner(); // Assume this method returns the winner
        if (winner == 1) {
            System.out.println("Congratulations! You win! 🎉");
        } else if (winner == -1) {
            System.out.println("CPU wins! Better luck next time. 🤖");
        } else {
            System.out.println("It's a draw! 🤝");
        }
    }

    private M parseMove(String input) {
        // Convert user input to an M type move (assumes integer moves for simplicity)
        return (M) Integer.valueOf(input);
    }
}

