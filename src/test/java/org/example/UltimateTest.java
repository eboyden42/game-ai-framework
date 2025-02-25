package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UltimateTest {
    private Ultimate ultimate;

    @BeforeEach
    public void setUp() {
        ultimate = new Ultimate();
    }

    @Test
    public void testUltimateConstructor() {
        Ultimate ultimate = new Ultimate();

        // Check that boardInPlay is initialized to -1
        assertEquals(-1, ultimate.getBoardInPlay(), "boardInPlay should be initialized to -1");

        // Check that each miniBoard is initialized and blank
        for (int i = 0; i < 9; i++) {
            assertNotNull(ultimate.getMiniBoard(i), "MiniBoard at index " + i + " should not be null");
            assertTrue(ultimate.getMiniBoard(i).isBlank(), "MiniBoard at index " + i + " should be blank");
        }
    }

    @Test
    public void testUltimateConstructorWithMove() {
        // Create an initial board setup
        MiniBoard[] originalBoards = new MiniBoard[9];
        for (int i = 0; i < 9; i++) {
            originalBoards[i] = new MiniBoard();
        }

        // Simulate a pre-existing move
        originalBoards[0].place(0, 0, 1); // Assume player 1 placed a move at (0,0) in board 0

        // Create a new Ultimate board based on the previous board with an additional move
        Ultimate ultimate = new Ultimate(originalBoards, 1, 1, 1, 2); // Player 2 places at (1,1) in board 1

        // Ensure the previous move is still there
        assertEquals(1, ultimate.getMiniBoard(0).getBoard()[0], "Original move at (0,0) in board 0 should be retained");

        // Ensure the new move was added correctly
        assertEquals(2, ultimate.getMiniBoard(1).getBoard()[4], "New move at (1,1) in board 1 should be added");

        // Ensure that other miniBoards remain unchanged
        for (int i = 0; i < 9; i++) {
            if (i != 0 && i != 1) {
                assertTrue(ultimate.getMiniBoard(i).isBlank(), "MiniBoard at index " + i + " should remain blank");
            }
        }
    }

    @Test
    public void testPlaceUpdatesMiniBoardAndBoardInPlay() {
        // Player 1 places a piece in board index 0, at row 1, col 1
        ultimate.place(0, 1, 1, 1);

        // Ensure the move was placed correctly
        assertEquals(1, ultimate.getMiniBoard(0).getBoard()[4], "Player 1's move should be at (1,1) in board 0");

        // Check that boardInPlay is updated correctly
        assertEquals(4, ultimate.getBoardInPlay(), "boardInPlay should be updated to board index 4");
    }

    @Test
    public void testPlaceUpdatesBoardInPlayToNegativeOneWhenNextBoardIsFull() {
        // Manually fill board index 4 to simulate a full board
        MiniBoard fullBoard = new MiniBoard();
        for (int i = 0; i < 9; i++) {
            fullBoard.getBoard()[i] = 1; // Assume Player 1 filled the board
        }
        ultimate.getBigBoard()[4] = fullBoard;

        // Place a move that would normally send play to board 4
        ultimate.place(0, 1, 1, 2);

        // Since board 4 is full, boardInPlay should be -1
        assertEquals(-1, ultimate.getBoardInPlay(), "boardInPlay should be -1 if the next board is full");
    }

    @Test
    public void testMovePossibleWhenBoardInPlayIsNegativeOne() {
        // When boardInPlay is -1, any move should be possible in an empty space
        ultimate.boardInPlay = -1;
        assertTrue(ultimate.isMovePossible(0, 1, 1), "Move should be possible when boardInPlay is -1 and space is empty");
    }

    @Test
    public void testMovePossibleWhenBoardInPlayMatchesIndex() {
        // Set boardInPlay to 2, meaning the move must be in miniBoard[2]
        ultimate.boardInPlay = 2;
        assertTrue(ultimate.isMovePossible(2, 0, 0), "Move should be possible in board 2");
        assertFalse(ultimate.isMovePossible(3, 0, 0), "Move should not be possible in board 3");
    }

    @Test
    public void testMoveNotPossibleWhenSpotIsTaken() {
        // Place a piece at (0,0) in board 2
        ultimate.place(2, 0, 0, 1);

        // Set boardInPlay to 2
        ultimate.boardInPlay = 2;

        // Move should not be possible in (0,0) of board 2 since it's occupied
        assertFalse(ultimate.isMovePossible(2, 0, 0), "Move should not be possible if the space is already occupied");
    }

    @Test
    public void testGenerateMovesWhenBoardInPlayIsNegativeOne() {
        // When boardInPlay is -1, all empty spaces in all miniBoards should generate moves
        ultimate.boardInPlay = -1;

        ArrayList<Ultimate> moves = ultimate.generateMoves(1);

        // There are 9 boards, each with 9 spaces = 81 total possible moves initially
        assertEquals(81, moves.size(), "Should generate 81 possible moves for an empty board.");
    }

    @Test
    public void testGenerateMovesWhenBoardInPlayIsRestricted() {
        // Set boardInPlay to 3, so moves should only be generated in MiniBoard[3]
        ultimate.boardInPlay = 3;

        ArrayList<Ultimate> moves = ultimate.generateMoves(2);

        // Only 9 possible moves should be generated from MiniBoard[3]
        assertEquals(9, moves.size(), "Should generate 9 moves for board 3.");
    }

    @Test
    public void testGenerateMovesWithOccupiedSpaces() {
        // Set boardInPlay to 4 and place a piece in the center of MiniBoard[4]
        ultimate.place(4, 1, 1, 1);

        ArrayList<Ultimate> moves = ultimate.generateMoves(2);

        // Since (1,1) is occupied, there should be 8 moves instead of 9
        assertEquals(8, moves.size(), "Should generate 8 moves since one space is occupied.");
    }

    @Test
    public void testWinnerWhenNoOneHasWon() {
        // No moves made, so there should be no winner
        assertEquals(0, ultimate.winner(), "There should be no winner initially.");
    }

    @Test
    public void testWinnerForPlayer1() {
        // Player 1 wins with a horizontal row on the big board (e.g., MiniBoards [0, 1, 2] are won by Player 1)

        // Simulate Player 1 winning in MiniBoards [0, 1, 2]
        ultimate.getBigBoard()[0].setWinner(1);
        ultimate.getBigBoard()[4].setWinner(1);
        ultimate.getBigBoard()[8].setWinner(1);

        // Player 1 should win the game
        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Diagonal_TopRightToBottomLeft() {
        // Player 1 wins on the diagonal from top-right to bottom-left
        ultimate.getBigBoard()[2].setWinner(1);
        ultimate.getBigBoard()[4].setWinner(1);
        ultimate.getBigBoard()[6].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Horizontal_TopRow() {
        // Player 1 wins in the top row
        ultimate.getBigBoard()[0].setWinner(1);
        ultimate.getBigBoard()[1].setWinner(1);
        ultimate.getBigBoard()[2].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Horizontal_MiddleRow() {
        // Player 1 wins in the middle row
        ultimate.getBigBoard()[3].setWinner(1);
        ultimate.getBigBoard()[4].setWinner(1);
        ultimate.getBigBoard()[5].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Horizontal_BottomRow() {
        // Player 1 wins in the bottom row
        ultimate.getBigBoard()[6].setWinner(1);
        ultimate.getBigBoard()[7].setWinner(1);
        ultimate.getBigBoard()[8].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Vertical_LeftColumn() {
        // Player 1 wins in the left column
        ultimate.getBigBoard()[0].setWinner(1);
        ultimate.getBigBoard()[3].setWinner(1);
        ultimate.getBigBoard()[6].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Vertical_MiddleColumn() {
        // Player 1 wins in the middle column
        ultimate.getBigBoard()[1].setWinner(1);
        ultimate.getBigBoard()[4].setWinner(1);
        ultimate.getBigBoard()[7].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer1_Vertical_RightColumn() {
        // Player 1 wins in the right column
        ultimate.getBigBoard()[2].setWinner(1);
        ultimate.getBigBoard()[5].setWinner(1);
        ultimate.getBigBoard()[8].setWinner(1);

        assertEquals(1, ultimate.winner(), "Player 1 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Diagonal_TopLeftToBottomRight() {
        // Player 2 wins on the diagonal from top-left to bottom-right
        ultimate.getBigBoard()[0].setWinner(2);
        ultimate.getBigBoard()[4].setWinner(2);
        ultimate.getBigBoard()[8].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Diagonal_TopRightToBottomLeft() {
        // Player 2 wins on the diagonal from top-right to bottom-left
        ultimate.getBigBoard()[2].setWinner(2);
        ultimate.getBigBoard()[4].setWinner(2);
        ultimate.getBigBoard()[6].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Horizontal_TopRow() {
        // Player 2 wins in the top row
        ultimate.getBigBoard()[0].setWinner(2);
        ultimate.getBigBoard()[1].setWinner(2);
        ultimate.getBigBoard()[2].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Horizontal_MiddleRow() {
        // Player 2 wins in the middle row
        ultimate.getBigBoard()[3].setWinner(2);
        ultimate.getBigBoard()[4].setWinner(2);
        ultimate.getBigBoard()[5].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Horizontal_BottomRow() {
        // Player 2 wins in the bottom row
        ultimate.getBigBoard()[6].setWinner(2);
        ultimate.getBigBoard()[7].setWinner(2);
        ultimate.getBigBoard()[8].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Vertical_LeftColumn() {
        // Player 2 wins in the left column
        ultimate.getBigBoard()[0].setWinner(2);
        ultimate.getBigBoard()[3].setWinner(2);
        ultimate.getBigBoard()[6].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Vertical_MiddleColumn() {
        // Player 2 wins in the middle column
        ultimate.getBigBoard()[1].setWinner(2);
        ultimate.getBigBoard()[4].setWinner(2);
        ultimate.getBigBoard()[7].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerForPlayer2_Vertical_RightColumn() {
        // Player 2 wins in the right column
        ultimate.getBigBoard()[2].setWinner(2);
        ultimate.getBigBoard()[5].setWinner(2);
        ultimate.getBigBoard()[8].setWinner(2);

        assertEquals(2, ultimate.winner(), "Player 2 should be the winner.");
    }

    @Test
    public void testWinnerWhenNoWinner() {
        // Ensure no winner when no three-in-a-row is formed
        ultimate.getBigBoard()[0].setWinner(2);
        ultimate.getBigBoard()[1].setWinner(1);
        ultimate.getBigBoard()[2].setWinner(2);
        ultimate.getBigBoard()[3].setWinner(2);
        ultimate.getBigBoard()[4].setWinner(1);
        ultimate.getBigBoard()[5].setWinner(2);
        ultimate.getBigBoard()[6].setWinner(1);
        ultimate.getBigBoard()[7].setWinner(2);
        ultimate.getBigBoard()[8].setWinner(1);

        assertEquals(0, ultimate.winner(), "There should be no winner.");
    }
}
