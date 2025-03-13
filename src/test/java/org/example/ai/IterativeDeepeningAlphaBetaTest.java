package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IterativeDeepeningAlphaBetaTest {
    MockGameState mockGameState;
    IterativeDeepeningAlphaBeta<String> iterativeDeepeningAlphaBeta;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        iterativeDeepeningAlphaBeta = new IterativeDeepeningAlphaBeta<>(500);
    }

    @Test
    public void testAlphaBeta() {
        String bestMove = iterativeDeepeningAlphaBeta.findBestMove(mockGameState);

        assertEquals("A", bestMove, "Iterative Deepening Alpha Beta algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testAlphaBetaMaximizingPlayer() {
        int score = iterativeDeepeningAlphaBeta.alphabeta(mockGameState, 3, -100000, 100000, true, System.currentTimeMillis());
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testAlphaBetaMinimizingPlayer() {
        int score = iterativeDeepeningAlphaBeta.alphabeta(mockGameState, 3, -100000, 100000,false, System.currentTimeMillis());
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
