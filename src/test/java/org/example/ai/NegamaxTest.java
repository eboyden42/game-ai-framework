package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegamaxTest {
    MockGameState mockGameState;
    Negamax<String> negamax;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        negamax = new Negamax<>(3);
    }

    @Test
    public void testAlphaBeta() {
        String bestMove = negamax.findBestMove(mockGameState);

        assertEquals("A", bestMove, "AlphaBeta algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testAlphaBetaMaximizingPlayer() {
        int score = negamax.negamax(mockGameState, 3);
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testAlphaBetaMinimizingPlayer() {
        int score = -negamax.negamax(mockGameState, 3);
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
