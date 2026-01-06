package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IterativeDeepeningMinimaxAlphaBetaTest {
    MockGameState mockGameState;
    IterativeDeepeningMinimaxAlphaBeta<String> iterativeDeepeningMinimaxAlphaBeta;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        iterativeDeepeningMinimaxAlphaBeta = new IterativeDeepeningMinimaxAlphaBeta<>(500);
    }

    @Test
    public void testFindBestMove() {
        String bestMove = iterativeDeepeningMinimaxAlphaBeta.findBestMove(mockGameState);
        assertEquals("A", bestMove, "Iterative Deepening Alpha Beta algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testMaximizingPlayer() {
        MockGameState maximizingState = new MockGameState();
        iterativeDeepeningMinimaxAlphaBeta.setRootPlayer(1);
        int score = iterativeDeepeningMinimaxAlphaBeta.alphabeta(maximizingState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, System.currentTimeMillis());
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testMinimizingPlayer() {
        MockGameState minimizingPlayer = new MockGameState();
        minimizingPlayer.applyMove("C");
        iterativeDeepeningMinimaxAlphaBeta.setRootPlayer(2);
        int score = iterativeDeepeningMinimaxAlphaBeta.alphabeta(minimizingPlayer, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, System.currentTimeMillis());
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
