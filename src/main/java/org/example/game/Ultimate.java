package org.example.game;

import java.io.*;
import java.util.*;

public class Ultimate implements GameState<UltimateMove>, Serializable {
    /**
     * This class implements the GameState interface to play the game
     * <a href="https://en.wikipedia.org/wiki/Ultimate_tic-tac-toe">Ultimate Tic-Tac-Toe</a>.
     *
     * @author Eli Boyden, eboyden42
     */

    /**
     * The array representing the 3x3 grid of mini-boards in the Ultimate Tic-Tac-Toe game.
     */
    private MiniBoard[] bigBoard = new MiniBoard[9];

    /**
     * Indicates which mini-board is currently in play.
     * A value of -1 means no restriction on the board selection.
     */
    private int boardInPlay;

    /**
     * Tracks the current player.
     * Typically, {@code 1} represents Player 1 and {@code 2} represents Player 2.
     */
    private int currentPlayer = 1;

    /**
     * Constructs a default Ultimate board.
     * Initializes all mini-boards as blank and sets {@code boardInPlay} to -1 (no restriction).
     */
    public Ultimate() {
        boardInPlay = -1;
        for (int i = 0; i < bigBoard.length; i ++) {
            bigBoard[i] = new MiniBoard();
        }
    }

    /**
     * Constructs an Ultimate board by copying a previous board and adding a new move.
     *
     * @param otherBigBoard The existing array of mini-boards to copy.
     * @param index     The board index where the move is placed (0 to 8).
     * @param row       The row position of the move (0 to 2).
     * @param col       The column position of the move (0 to 2).
     */
    public Ultimate(MiniBoard[] otherBigBoard, int index, int row, int col) {
        boardInPlay = -1;
        for (int i = 0; i < bigBoard.length; i ++) {
            bigBoard[i] = new MiniBoard();
        }
        for (int i = 0; i < bigBoard.length; i ++) {
            for (int n = 0; n < bigBoard[0].getBoard().length; n ++) {
                bigBoard[i].getBoard()[n] = otherBigBoard[i].getBoard()[n];
            }
        }
        this.applyMove(new UltimateMove(index, row, col));
    }

    /**
     * Prints the current state of the board to the terminal.
     */
    public void print() {
        for (int start = 0; start < 9; start += 3) {
            for (int level = 0; level < 3; level ++) {
                for (int board = start; board < (start+3); board ++){
                    bigBoard[board].printOneRow(level);
                    if (board % 3 != 2) {
                        System.out.print("|");
                    }
                }
                System.out.println();
            }
            if (start != 6) {
                System.out.println("-----------");
            }
        }
    }

    /**
     * Places a piece on a copy of the board, updates the board in play, switches players and returns that copy.
     *
     * @param move The move containing the board index [0-8], row [0-2], and column [0-2].
     * @return A new {@code Ultimate} board reflecting the applied move.
     */
    public Ultimate applyMove(UltimateMove move) {

        Ultimate copy = deepCopy();

        int bigIndex = move.getIndex();
        int row = move.getRow();
        int col = move.getCol();

        copy.getBigBoard()[bigIndex].place(row, col, currentPlayer);
        copy.update();

        if (copy.getBigBoard()[row*3+col].isBoardFull()){
            copy.setBoardInPlay(-1);
        }
        else {
            copy.setBoardInPlay(row*3+col);
        }

        // Switch players
        if (copy.getCurrentPlayer() == 1) {
            copy.currentPlayer = 2;
        } else {
            copy.currentPlayer = 1;
        }

        return copy;
    }

    /**
     * Creates a deep copy of the Ultimate board using serialization.
     *
     * @return A new {@code Ultimate} object that is an independent copy of the original.
     */
    public Ultimate deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (Ultimate) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Deep copy failed", e);
        }
    }

    /**
     * Checks if a move is possible based on board constraints.
     * A move is valid if:
     * 1. The location is within the board in play or unrestricted ({@code boardInPlay == -1}).
     * 2. The inputs are within valid ranges: {@code 0 <= index <= 8}, {@code 0 <= row, col <= 2}.
     * 3. The target location is unoccupied.
     *
     * @param index The board index (0 to 8).
     * @param row   The row position (0 to 2).
     * @param col   The column position (0 to 2).
     * @return {@code true} if the move is valid, otherwise {@code false}.
     */
    public boolean isMovePossible(int index, int row, int col) {
        if (index < 0 || index > 8 || row < 0 || row > 2 || col < 0 || col > 2) { // Make sure input is valid
            return false;
        }
        if (index == boardInPlay || boardInPlay == -1) { // Check if the square is already filled
            if (bigBoard[index].getBoard()[row*3+col] == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a list of all possible moves from the current board state.
     *
     * @return An {@code ArrayList} of {@code UltimateMove} objects representing valid next moves.
     */
    public ArrayList<UltimateMove> getPossibleMoves() {
        ArrayList<UltimateMove> moves = new ArrayList<>();

        // If any board can be played in, check them all
        if (boardInPlay == -1) {
            for (int index = 0; index < bigBoard.length; index ++) {
                //if the board is full or there is a winner for it, do nothing
                if (!bigBoard[index].isBoardFull() && (bigBoard[index].getWinner() == 0)) {
                    for (int row = 0; row < 3; row ++) {
                        for (int col = 0; col < 3; col ++) {
                            if (this.isMovePossible(index, row, col)) {
                                moves.add(new UltimateMove(index, row, col));
                            }
                        }
                    }
                }
            }
        }
        else { // If the board is restricted, only search that board
            for (int row = 0; row < 3; row ++) {
                for (int col = 0; col < 3; col ++) {
                    if (bigBoard[boardInPlay].getBoard()[row*3+col] == 0) {
                        moves.add(new UltimateMove(boardInPlay, row, col));
                    }
                }
            }
        }

        return moves;
    }

    /**
     * Evaluates the current board state to determine the winner.
     *
     * @return 0 if there is no winner, 1 if Player 1 has won, 2 if Player 2 has won.
     */
    public int evaluateWinner() {
        // This method works by calculating products.
        // For instance, if you multiply the values of the first row and get a 1, payer 1 has won,
        // an 8 indicates that player 2 has won, and any other number indicates no winner.

        int rowProduct = 1;
        int[] colProducts = {1, 1, 1};
        int[] diagProducts = {1, 1};


        for (int i = 0; i < 9; i ++) {

            // Checking rows
            rowProduct *= bigBoard[i].winner();
            if (i % 3 == 2) {
                if (rowProduct != 0) {
                    if (rowProduct == 8) {
                        return 2;
                    }
                    else if (rowProduct == 1){
                        return 1;
                    }
                }
                rowProduct = 1;
            }

            // Checking columns
            colProducts[i%3] *= bigBoard[i].getWinner();
        }

        // Checking columns continued
        for (int c : colProducts) {
            if (c == 8) {
                return 2;
            }
            else if (c == 1) {
                return 1;
            }
        }


        // Checking diagonals
        diagProducts[0] *= bigBoard[2].getWinner() * bigBoard[4].getWinner() * bigBoard[6].getWinner();
        diagProducts[1] *= bigBoard[0].getWinner() * bigBoard[4].getWinner() * bigBoard[8].getWinner();

        for (int i : diagProducts) {
            if (i != 0) {
                if (i == 1) {
                    return 1;
                }
                else if (i == 8) {
                    return 2;
                }
            }
        }

        // Otherwise return 0
        return 0;
    }

    /**
     * Checks if the game has reached a terminal state.
     * A game is terminal if there is a winner (non-zero) or the board is full.
     *
     * @return {@code true} if the game is over, otherwise {@code false}.
     */
    @Override
    public boolean isTerminal() {
        return (this.evaluateWinner() != 0) || this.isAllFull();
    }

    /**
     * Returns the array of mini-boards representing the Ultimate board.
     *
     * @return The {@code MiniBoard} array representing the current state of the board.
     */
    public MiniBoard[] getBigBoard() {
        return bigBoard;
    }

    /**
     * Retrieves the mini-board at the specified index.
     *
     * @param index The index of the mini-board [0 to 8].
     * @return The {@code MiniBoard} at the given index.
     */
    public MiniBoard getMiniBoard(int index) {
        return bigBoard[index];
    }

    /**
     * Sums the number of open 2-in-a-rows for a given player across all mini-boards.
     * Helper method for the evaluation function.
     *
     * @param player The player for whom the 2-in-a-rows are counted (1 or 2).
     * @return The total number of open 2-in-a-rows for the specified player.
     */
    public int numberOfTwos(int player) {
        int num = 0;
        for (int i = 0; i < bigBoard.length; i ++) {
            num += bigBoard[i].numberTwos(player);
        }
        return num;
    }

    /**
     * Sums the number of mini-boards that the specified player has won.
     * Helper method for the evaluation function.
     *
     * @param player The player for whom the winning mini-boards are counted (1 or 2).
     * @return The total number of mini-boards won by the specified player.
     */
    public int numberOfThrees(int player) {
        int num = 0;
        for (int i = 0; i < bigBoard.length; i ++) {
            if (bigBoard[i].winner() == player) {
                num ++;
            }
        }
        return num;
    }

    /**
     * Sums the number of times the specified player has won 2 mini-boards in a row on the Ultimate board,
     * with the third mini-board still open.
     * Helper method for the evaluation function.
     *
     * @param player The player for whom the open 2-in-a-row configurations are counted (1 or 2).
     * @return The total number of 2-in-a-row configurations for the specified player across all rows of mini-boards.
     */
    public int numberOfBigTwos(int player) {
        int num = 0;
        for (int i = 0; i < 6; i +=3) {
            if (bigBoard[i].winner() == player && bigBoard[i+1].winner() == player && bigBoard[i+2].winner() == 0) {
                num ++;
            }
            if (bigBoard[i].winner() == player && bigBoard[i+1].winner() == 0 && bigBoard[i+2].winner() == player) {
                num ++;
            }
            if (bigBoard[i].winner() == 0 && bigBoard[i+1].winner() == player && bigBoard[i+2].winner() == player) {
                num ++;
            }
        }

        for (int i = 0; i < 3; i ++) {
            if (bigBoard[i].winner() == player && bigBoard[i+3].winner() == player && bigBoard[i+6].winner() == 0) {
                num ++;
            }
            if (bigBoard[i].winner() == player && bigBoard[i+3].winner() == 0 && bigBoard[i+6].winner() == player) {
                num ++;
            }
            if (bigBoard[i].winner() == 0 && bigBoard[i+3].winner() == player && bigBoard[i+6].winner() == player) {
                num ++;
            }
        }

        if (bigBoard[0].winner() == player && bigBoard[4].winner() == player && bigBoard[8].winner() == 0) {
            num ++;
        }
        if (bigBoard[0].winner() == player && bigBoard[4].winner() == 0 && bigBoard[8].winner() == player) {
            num ++;
        }
        if (bigBoard[0].winner() == 0 && bigBoard[4].winner() == player && bigBoard[8].winner() == player) {
            num ++;
        }

        if (bigBoard[2].winner() == player && bigBoard[4].winner() == player && bigBoard[6].winner() == 0) {
            num ++;
        }
        if (bigBoard[2].winner() == player && bigBoard[4].winner() == 0 && bigBoard[6].winner() == player) {
            num ++;
        }
        if (bigBoard[2].winner() == 0 && bigBoard[4].winner() == player && bigBoard[6].winner() == player) {
            num ++;
        }

        return num;
    }

    /**
     * Evaluates the current state from the perspective of a given player.
     *
     * @param player the player whose perspective is considered.
     * @return an evaluation score (higher is better for the player).
     */
    public int evaluate(int player) {
        int eval = 0;

        if (evaluateWinner() != 0) {
            if (evaluateWinner() == player) {
                return 10000;
            }
            else {
                return -10000;
            }
        }

        int twoMultiplier = 20;

        eval += numberOfTwos(player) * twoMultiplier;
        eval -= numberOfTwos(-player + 3) * twoMultiplier;

        int threeMultiplier = 50;

        eval += numberOfThrees(player) * threeMultiplier;
        eval -= numberOfThrees(-player + 3) * threeMultiplier;

        int bigTwoMultiplier = 100;

        eval += numberOfBigTwos(player) * bigTwoMultiplier;
        eval -= numberOfBigTwos(-player + 3) * bigTwoMultiplier;

        return eval;
    }

    /**
     * Checks if all mini-boards in the Ultimate board are full.
     *
     * @return {@code true} if all mini-boards are full, otherwise {@code false}.
     */
    public boolean isAllFull() {
        for (int i = 0; i < bigBoard.length; i ++) {
            if (!bigBoard[i].isBoardFull()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the current board in play for testing purposes.
     *
     * @param boardInPlay The index of the mini-board to be set as the board in play (0 to 8).
     */
    protected void setBoardInPlay(int boardInPlay) {
        this.boardInPlay = boardInPlay;
    }

    /**
     * Returns the index of the board currently in play.
     * A value of -1 means there are no restrictions on the board selection.
     *
     * @return The index of the board in play, or -1 if no restrictions.
     */
    public int getBoardInPlay() {
        return boardInPlay;
    }

    /**
     * Returns the current player.
     *
     * @return The current player (1 or 2).
     */
    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets player input and converts it to a move. If the input of the player is not valid move, return an empty optional.
     * Invalid moves in this case are inputs that cannot be parsed into a type M object and moves that violate the game rules
     * at the current state of the game (using getPossibleMoves() can be useful).
     *
     * Getting user input should be user-friendly with descriptive yet concise directions.
     *
     * @return Optional<UltimateMove> type based on user input
     */
    public Optional<UltimateMove> getPlayerInputMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Board of Play: %d\n", boardInPlay);
        try {
            int board = boardInPlay;
            if (boardInPlay == -1) {
                System.out.println("Enter Board Number: ");
                board = scanner.nextInt();
            }
            System.out.println("Enter Row Number: ");
            int row = scanner.nextInt();
            System.out.println("Enter Col Number: ");
            int col = scanner.nextInt();
            if (isMovePossible(board, row, col)) {
                return Optional.of(new UltimateMove(board, row, col));
            }
            return Optional.empty();
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Updates the mini-boards based on whether a player has won them or not.
     */
    public void update() {
        for (int i = 0; i < bigBoard.length; i ++) {
            int win = bigBoard[i].winner();
            if (win != 0) {
                bigBoard[i].setWinner(win);
            }
        }
    }

    /**
     * Sets a winner manually for a specified mini-board.
     *
     * @param board The index of the mini-board (0 to 8).
     * @param player The player who is the winner (1 or 2).
     */
    protected void setBigWinner(int board, int player) {
        bigBoard[board].setWinner(player);
    }

    /**
     * Compares this Ultimate board with another object for equality.
     * Two Ultimate boards are considered equal if they have the same mini-board states,
     * the same board in play, and the same current player.
     *
     * @param o The object to compare this Ultimate board against.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ultimate ultimate = (Ultimate) o;
        return boardInPlay == ultimate.boardInPlay && currentPlayer == ultimate.currentPlayer && Arrays.equals(bigBoard, ultimate.bigBoard);
    }
}
