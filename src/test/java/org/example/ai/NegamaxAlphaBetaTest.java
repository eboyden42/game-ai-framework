package org.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegamaxAlphaBetaTest {
    MockGameState mockGameState;
    NegamaxAlphaBeta<String> negamaxAlphaBeta;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        negamaxAlphaBeta = new NegamaxAlphaBeta<>(3);
    }

    @Test
    public void testFindBestMove() {
        String bestMove = negamaxAlphaBeta.findBestMove(mockGameState);

        assertEquals("A", bestMove, "AlphaBeta algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testMaximizingPlayer() {
        int score = negamaxAlphaBeta.negamaxAlphaBeta(mockGameState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testMinimizingPlayer() {
        int score = -negamaxAlphaBeta.negamaxAlphaBeta(mockGameState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
