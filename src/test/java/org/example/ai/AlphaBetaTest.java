package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlphaBetaTest {
    MockGameState mockGameState;
    AlphaBeta<String> alphabeta;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        alphabeta = new AlphaBeta<String>(3);
    }

    @Test
    public void testAlphaBeta() {
        String bestMove = alphabeta.findBestMove(mockGameState);

        assertEquals("A", bestMove, "AlphaBeta algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testAlphaBetaMaximizingPlayer() {
        int score = alphabeta.alphabeta(mockGameState, 3, -100000, 100000, true);
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testAlphaBetaMinimizingPlayer() {
        int score = alphabeta.alphabeta(mockGameState, 3, -100000, 100000,false);
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
