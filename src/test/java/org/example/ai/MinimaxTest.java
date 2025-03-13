package org.example.ai;

import org.example.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinimaxTest {
    MockGameState mockGameState;
    Minimax<String> minimax;

    @BeforeEach
    public void setup() {
        mockGameState = new MockGameState();
        minimax = new Minimax<>(3);
    }

    @Test
    public void testMinimax() {
        String bestMove = minimax.findBestMove(mockGameState);

        assertEquals("A", bestMove, "Minimax algorithm failed to find the best move in a mocked game scenario");
    }

    @Test
    void testMinimaxMaximizingPlayer() {
        int score = minimax.minimax(mockGameState, 3, true);
        assertEquals(10, score, "Expected score for maximizing player at depth 3");
    }

    @Test
    void testMinimaxMinimizingPlayer() {
        int score = minimax.minimax(mockGameState, 3, false);
        assertEquals(-10, score, "Expected score for minimizing player at depth 3");
    }
}
