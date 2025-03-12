package org.example.game;
import java.io.Serializable;
import java.util.*;

public class MiniBoard implements Serializable {
    /**
     * This class acts as a regular tic-tac-toe board to be used in the Ultimate.java implementation of the game
     * <a href="https://en.wikipedia.org/wiki/Ultimate_tic-tac-toe">Ultimate Tic-Tac-Toe</a>.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represents the current state of the board.
     * The board is an array of integers where each element corresponds to a position on the board:
     * 0 = empty, 1 = player O, 2 = player X.
     */
    private int[] board = new int[9];

    /**
     * Flag indicating whether the board is full.
     * This is set to {@code true} if all positions on the board are occupied, {@code false} otherwise.
     */
    private boolean isFull = false;

    /**
     * Represents the winner of the game.
     * 0 indicates no winner, 1 indicates player O has won, and 2 indicates player X has won.
     */
    private int winner = 0; // 1 = O and 2 = X

    /**
     * Constructs a default board.
     * Initializes {@code board} to all zeros, {@code isFull} to false and {@code winner} to zero.
     */
    public MiniBoard() {}

    /**
     * Constructs a new MiniBoard by copying the state of another MiniBoard and placing a piece on it.
     * The new board inherits the state (board, winner, and fullness) from the given {@code otherMiniBoard},
     * and then places the specified player's piece at the given row and column.
     *
     * @param otherMiniBoard The MiniBoard to copy the state from.
     * @param row The row index [0-2] where the piece will be placed.
     * @param col The column index [0-2] where the piece will be placed.
     * @param player The player making the move (1 for player O, 2 for player X).
     */
    protected MiniBoard(MiniBoard otherMiniBoard, int row, int col, int player) {
        board = otherMiniBoard.getBoard();
        winner = otherMiniBoard.getWinner();
        isFull = otherMiniBoard.isBoardFull();

        this.place(row, col, player);
    }

    /**
     * Constructs a MiniBoard using a given board array.
     * This constructor is primarily used for testing purposes and initializes the board state
     * with the provided array of integers.
     *
     * @param board The array representing the board state. Each element corresponds to a position on the board:
     * 0 = empty, 1 = player O, 2 = player X.
     */
    protected MiniBoard(int[] board) { //used for testing
        this.board = board;
    }

    /**
     * Places a player's piece on the MiniBoard at the specified row and column.
     * If the given position is valid and unoccupied, the piece is placed; otherwise, an exception is thrown.
     *
     * @param row The row index (0-2) where the piece will be placed.
     * @param col The column index (0-2) where the piece will be placed.
     * @param player The player making the move (1 for player O, 2 for player X).
     * @return The current MiniBoard instance with the updated piece placement.
     * @throws IllegalArgumentException If the row or column is out of bounds (not between 0 and 2).
     * @throws UnsupportedOperationException If the specified square already contains a piece.
     */
    public MiniBoard place(int row, int col, int player) {
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            throw new IllegalArgumentException("Attempting to place a piece with invalid rows and columns.");
        }
        if (board[row * 3 + col] == 0) {
            board[row * 3 + col] = player;
        } else {
            throw new UnsupportedOperationException("There is already a piece in that square.");
        }
        return this;
    }

    /**
     * Prints out a visualization of the mini-board.
     */
    public void print() {
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.print(playerToChar(board[i]));
            if (i % 3 != 0) {
                System.out.print("|");
            }
            if (i == 2 || i == 5) {
                System.out.println("\n________");
            }
        }
        System.out.println();
    }

    /**
     * Evaluates the current board state to determine the winner.
     *
     * @return 0 if there is no winner, 1 if Player 1 has won, 2 if Player 2 has won.
     */
    public int winner() {
        if (winner != 0) { // Check to see if calculation is necessary
            return winner;
        }

        // This method works by calculating products.
        // For instance, if you multiply the values of the first row and get a 1, payer 1 has won,
        // an 8 indicates that player 2 has won, and any other number indicates no winner.

        int rowProduct = 1;
        int[] colProducts = {1, 1, 1};
        int[] diagProducts = {1, 1};


        for (int i = 0; i < 9; i++) {

            // Checking rows
            rowProduct *= board[i];
            if (i % 3 == 2) {
                if (rowProduct != 0) {
                    if (rowProduct == 8) {
                        setWinner(2);
                        return 2;
                    } else if (rowProduct == 1) {
                        setWinner(1);
                        return 1;
                    }
                }
                rowProduct = 1;
            }

            // Checking columns
            colProducts[i % 3] *= board[i];
        }

        // Checking columns continued
        for (int c : colProducts) {
            if (c == 8) {
                setWinner(2);
                return 2;
            }
            if (c == 1) {
                setWinner(1);
                return 1;
            }
        }


        // Checking diagonals
        diagProducts[0] *= board[2] * board[4] * board[6];
        diagProducts[1] *= board[0] * board[4] * board[8];

        for (int i : diagProducts) {
            if (i != 0) {
                if (i == 1) {
                    setWinner(1);
                    return 1;
                } else if (i == 8) {
                    setWinner(2);
                    return 2;
                }
            }
        }

        return 0;
    }

    /**
     * Sets a winner for the entire board and changes the pieces in that board to be all the winning piece. For instance
     * if setWinner(1) is called then each int in {@code board} would be set to 1.
     *
     * @param winningPlayer The player to declare the winner
     */
    public void setWinner(int winningPlayer) {

        isFull = true;
        winner = winningPlayer;

        for (int i = 0; i < board.length; i++) {
            board[i] = winningPlayer;
        }
    }

    /**
     * Returns a boolean value that represents whether a board is full or not.
     *
     * @return {@code true} if the board has no empty spaces, otherwise {@code false}.
     */
    public boolean isBoardFull() {
        if (isFull) {
            return isFull;
        } else {
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) {
                    return false;
                }
            }
            isFull = true;
            return true;
        }
    }

    /**
     * Returns the current state of the board as an array. For more details, see instance variable docs above.
     *
     * @return An integer array representing the current state of the board.
     */
    public int[] getBoard() {
        return board;
    }

    /**
     * Returns the instance variable flag that stores who is the winner. Use this method only when you are certain that
     * a piece will not be placed before the next MiniBoard.winner() call.
     *
     * @return An integer representing the winner of the game
     */
    public int getWinner() { return winner;}

    /**
     * Prints one row of the board. Used for printing an Ultimate tic-tac-toe board (see Ultimate.print()).
     *
     * @param row The of the board to print
     */
    public void printOneRow(int row) {
        for (int i = 0; i < board.length; i++) {
            if (i / 3 == row) {
                System.out.print(playerToChar(board[i]));
            }
        }
    }

    /**
     * Sums the number of open 2-in-a-rows for a given player. Helper method for the evaluation function.
     *
     * @param player The player for whom the 2-in-a-rows are counted (1 or 2).
     * @return The total number of open 2-in-a-rows for the specified player.
     */
    public int numberTwos(int player) {
        int num = 0;
        for (int i = 0; i < 6; i += 3) {
            if (board[i] == player && board[i + 1] == player && board[i + 2] == 0) {
                num++;
            }
            if (board[i] == player && board[i + 1] == 0 && board[i + 2] == player) {
                num++;
            }
            if (board[i] == 0 && board[i + 1] == player && board[i + 2] == player) {
                num++;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[i] == player && board[i + 3] == player && board[i + 6] == 0) {
                num++;
            }
            if (board[i] == player && board[i + 3] == 0 && board[i + 6] == player) {
                num++;
            }
            if (board[i] == 0 && board[i + 3] == player && board[i + 6] == player) {
                num++;
            }
        }

        if (board[0] == player && board[4] == player && board[8] == 0) {
            num++;
        }
        if (board[0] == player && board[4] == 0 && board[8] == player) {
            num++;
        }
        if (board[0] == 0 && board[4] == player && board[8] == player) {
            num++;
        }

        if (board[2] == player && board[4] == player && board[6] == 0) {
            num++;
        }
        if (board[2] == player && board[4] == 0 && board[6] == player) {
            num++;
        }
        if (board[2] == 0 && board[4] == player && board[6] == player) {
            num++;
        }

        return num;
    }

    /**
     * Converts a player number to its corresponding character representation.
     *
     * @param player The player number (1 for player O, 2 for player X).
     * @return A character representing the player: 'O' for player 1, 'X' for player 2, or a space (' ') for an invalid player.
     */
    public char playerToChar(int player) {
        if (player == 1) {
            return 'O';
        }
        if (player == 2) {
            return 'X';
        }
        return ' ';
    }

    /**
     * Checks if the board is completely empty (no pieces placed).
     *
     * @return {@code true} if all positions on the board are empty (represented by 0), {@code false} if there is at
     * least one piece placed on the board.
     */
    public boolean isBlank() {
        for (int i = 0; i < board.length; i ++) {
            if (board[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this MiniBoard to another object for equality.
     * Two MiniBoards are considered equal if they have the same board state,
     * the same fullness status, and the same winner.
     *
     * @param obj The object to compare to this MiniBoard.
     * @return {@code true} if the given object is equal to this MiniBoard, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MiniBoard other = (MiniBoard) obj;
        return Arrays.equals(board, other.board) && isFull == other.isFull && winner == other.winner;
    }

}