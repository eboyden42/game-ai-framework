package org.example.ai;

import org.example.game.GameState;

import java.util.List;
import java.util.Optional;

public class MockGameState implements GameState<String> {
    /**
     * Mock game state to test search algorithms. Has three moves A, B and C with evaluations 10, -10 and 0 respectively.
     * The evaluation of the game state is the sum of evaluations of moves played. For example, if a move sequence
     * was A, B, C; the evaluations for that state should be 10+(-10)+0=0.
     *
     * @author Eli Boyden, eboyden42
     */

    private int evaluation = 0;
    private int currentPlayer = 1;

    @Override
    public List<String> getPossibleMoves() {
        return List.of("A", "B", "C");
    }

    @Override
    public GameState<String> applyMove(String move) {
        MockGameState newGameState = new MockGameState();
        newGameState.currentPlayer = (this.currentPlayer % 2) + 1;
        newGameState.evaluation = this.evaluation;

        switch (move) {
            case "A":
                newGameState.evaluation = 10;
                break;
            case "B":
                newGameState.evaluation = -10;
                break;
            case "C":
                newGameState.evaluation = 0;
        }

        return newGameState;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int evaluate(int player) {
        return evaluation;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void print() {

    }

    @Override
    public int evaluateWinner() {
        return 0;
    }

    @Override
    public Optional<String> getPlayerInputMove() {
        return Optional.empty();
    }
}
