package org.example;

import org.example.game.MiniBoard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MiniBoardTest {

    @Test
    public void testPlace() {
        int[] expectedBoard = {0, 0, 2,
                               1, 0, 1,
                               0, 0, 2};
        MiniBoard expected = new MiniBoard(expectedBoard);

        MiniBoard test = new MiniBoard().place(1, 0, 1).place(1, 2, 1).place(0, 2, 2).place(2, 2, 2);

        assertEquals(expected, test, "Mini board place functionality not holding in equivalence case.");
        assertThrows(UnsupportedOperationException.class, () -> test.place(1, 0, 1), "Mini board not throwing expected exception.");
    }

    @Test
    public void testWinner() {
        // Case 1: Player 1 wins (horizontal)
        int[] boardP1Wins = {1, 1, 1,
                2, 0, 2,
                0, 0, 0};
        MiniBoard p1WinBoard = new MiniBoard(boardP1Wins);
        assertEquals(1, p1WinBoard.winner(), "Player 1 should be the winner.");

        // Case 2: Player 2 wins (vertical)
        int[] boardP2Wins = {2, 1, 0,
                2, 1, 0,
                2, 0, 1};
        MiniBoard p2WinBoard = new MiniBoard(boardP2Wins);
        assertEquals(2, p2WinBoard.winner(), "Player 2 should be the winner.");

        // Case 3: Tie (Full board with no winner)
        int[] boardTie = {1, 2, 1,
                1, 2, 2,
                2, 1, 1};
        MiniBoard tieBoard = new MiniBoard(boardTie);
        assertEquals(0, tieBoard.winner(), "The game should be a tie.");

        // Case 4: Player 1 wins (diagonal from top-left to bottom-right)
        int[] boardP1DiagonalWin = {1, 2, 0,
                2, 1, 0,
                0, 0, 1};
        MiniBoard p1DiagonalWinBoard = new MiniBoard(boardP1DiagonalWin);
        assertEquals(1, p1DiagonalWinBoard.winner(), "Player 1 should win diagonally.");

        // Case 5: Player 2 wins (diagonal from top-right to bottom-left)
        int[] boardP2DiagonalWin = {1, 2, 2,
                1, 2, 0,
                2, 0, 1};
        MiniBoard p2DiagonalWinBoard = new MiniBoard(boardP2DiagonalWin);
        assertEquals(2, p2DiagonalWinBoard.winner(), "Player 2 should win diagonally.");
    }

    @Test
    public void testIsBoardFull() {
        // Case 1: Full board (should return true)
        int[] fullBoard = {1, 2, 1,
                1, 2, 2,
                2, 1, 1};
        MiniBoard fullMiniBoard = new MiniBoard(fullBoard);
        assertTrue(fullMiniBoard.isBoardFull(), "Board should be full.");

        // Case 2: Empty board (should return false)
        int[] emptyBoard = {0, 0, 0,
                0, 0, 0,
                0, 0, 0};
        MiniBoard emptyMiniBoard = new MiniBoard(emptyBoard);
        assertFalse(emptyMiniBoard.isBoardFull(), "Board should not be full.");

        // Case 3: Partially filled board (should return false)
        int[] partialBoard = {1, 2, 0,
                0, 1, 2,
                2, 1, 0};
        MiniBoard partialMiniBoard = new MiniBoard(partialBoard);
        assertFalse(partialMiniBoard.isBoardFull(), "Board should not be full.");
    }

    @Test
    public void testNumberTwos() {
        // Case 1: Player 1 has two "two-in-a-row" situations
        int[] boardP1TwoInRow = {1, 1, 0,  // One two-in-a-row (1,1,0)
                                 2, 0, 2,
                                 1, 0, 1}; // One two-in-a-row (1,0,1)
        MiniBoard p1TwoInRowBoard = new MiniBoard(boardP1TwoInRow);
        assertEquals(2, p1TwoInRowBoard.numberTwos(1), "Player 1 should have two two-in-a-row situations.");

        // Case 2: Player 2 has two "two-in-a-row" situation
        int[] boardP2TwoInRow = {2, 0, 2,  // One two-in-a-row (2,0,2)
                                 1, 2, 0,
                                 1, 0, 0};// diagonal two-in-a-row
        MiniBoard p2TwoInRowBoard = new MiniBoard(boardP2TwoInRow);
        assertEquals(2, p2TwoInRowBoard.numberTwos(2), "Player 2 should have two two-in-a-row situations.");

        // Case 3: Player 1 has three two-in-a-row on a diagonal
        int[] boardP1Diagonal = {1, 2, 1,// one vertical
                                 2, 0, 0,
                                 1, 0, 1}; // 2 Two-in-a-row (1,0,1) diagonally
        MiniBoard p1DiagonalBoard = new MiniBoard(boardP1Diagonal);
        assertEquals(3, p1DiagonalBoard.numberTwos(1), "Player 1 should have three two-in-a-rows.");

        // Case 4: Player 2 has two-in-a-row in a column
        int[] boardP2Column = {2, 1, 0,
                               2, 0, 1, // Two-in-a-row in the first column (2,2,0)
                               0, 0, 1};
        MiniBoard p2ColumnBoard = new MiniBoard(boardP2Column);
        assertEquals(1, p2ColumnBoard.numberTwos(2), "Player 2 should have one two-in-a-row in a column.");

        // Case 5: No two-in-a-row situations
        int[] boardNoTwos = {1, 2, 1,
                             2, 1, 2,
                             2, 1, 2}; // No two-in-a-row cases
        MiniBoard noTwosBoard = new MiniBoard(boardNoTwos);
        assertEquals(0, noTwosBoard.numberTwos(1), "Player 1 should have zero two-in-a-row situations.");
        assertEquals(0, noTwosBoard.numberTwos(2), "Player 2 should have zero two-in-a-row situations.");
    }
}
