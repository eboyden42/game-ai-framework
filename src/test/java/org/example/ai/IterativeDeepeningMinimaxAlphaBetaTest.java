package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        int score = iterativeDeepeningMinimaxAlphaBeta.alphabeta(mockGameState, 3, -100000, 100000, true, System.currentTimeMillis());
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testMinimizingPlayer() {
        int score = iterativeDeepeningMinimaxAlphaBeta.alphabeta(mockGameState, 3, -100000, 100000,false, System.currentTimeMillis());
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
