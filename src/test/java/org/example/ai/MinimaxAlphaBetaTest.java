package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimaxAlphaBetaTest {
    MockGameState mockGameState;
    MinimaxAlphaBeta<String> alphabeta;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        alphabeta = new MinimaxAlphaBeta<String>(3);
    }

    @Test
    public void testAlphaBeta() {
        String bestMove = alphabeta.findBestMove(mockGameState);

        assertEquals("A", bestMove, "AlphaBeta algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testAlphaBetaMaximizingPlayer() {
        alphabeta.setRootPlayer(1);
        int score = alphabeta.alphabeta(mockGameState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testAlphaBetaMinimizingPlayer() {
        mockGameState.applyMove("C");
        alphabeta.setRootPlayer(2);
        int score = alphabeta.alphabeta(mockGameState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
