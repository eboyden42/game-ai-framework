package org.example.game;

import java.io.*;
import java.util.*;

public class Ultimate implements GameState<UltimateMove>, Serializable {

    private MiniBoard[] bigBoard = new MiniBoard[9];
    private int boardInPlay;
    private int currentPlayer = 1;

    //constructs a default ultimate board with board in play = -1 and each miniBoard initialized blank
    public Ultimate() {
        boardInPlay = -1;
        for (int i = 0; i < bigBoard.length; i ++) {
            bigBoard[i] = new MiniBoard();
        }
    }

    //constructs a board based on a previous board, with an additional move added
    public Ultimate(MiniBoard[] bigBoard2, int index, int row, int col) {
        boardInPlay = -1;
        for (int i = 0; i < bigBoard.length; i ++) {
            bigBoard[i] = new MiniBoard();
        }
        for (int i = 0; i < bigBoard.length; i ++) {
            for (int n = 0; n < bigBoard[0].getBoard().length; n ++) {
                bigBoard[i].getBoard()[n] = bigBoard2[i].getBoard()[n];
            }
        }
        this.applyMove(new UltimateMove(index, row, col));
    }

    //prints out the board to terminal
    public void print() {
        for (int start = 0; start < 9; start += 3) {
            for (int level = 0; level < 3; level ++) {
                for (int board = start; board < (start+3); board ++){
                    bigBoard[board].printOneLine(level);
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

    //places a piece given the bigBoard index [0, 8], and two row and col numbers both [0, 2]
    //also updates the board in play based on the placement of the piece
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

        //switch players
        if (copy.getCurrentPlayer() == 1) {
            copy.currentPlayer = 2;
        } else {
            copy.currentPlayer = 1;
        }

        return copy;
    }

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

    //given a certain move determine if it is possible namely
    // 1. the location that the piece will be placed is in the board in play, or there is no restriction (boardInPlay) == -1
    // 2. the location does not already have a piece placed there
    public boolean isMovePossible(int index, int row, int col) {
        if (index == boardInPlay || boardInPlay == -1) {
            if (bigBoard[index].getBoard()[row*3+col] == 0) {
                return true;
            }
        }
        return false;
    }

    //returns an arraylist of Ultimate states for each possible move that can be made from the current board state
    public ArrayList<UltimateMove> getPossibleMoves() {
        ArrayList<UltimateMove> moves = new ArrayList<>();

        //if any board can be played in, check them all
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
        else { //if the board is restricted, only search that board
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

    //determines if there is a winner for the game
    // 0 if there is no winner
    // 1 if player 1 has won
    // 2 if player 2 has won
    public int evaluateWinner() {
        int rowProduct = 1;
        int[] colProducts = {1, 1, 1};
        int[] diagProducts = {1, 1};


        for (int i = 0; i < 9; i ++) {

            //rows
            rowProduct *= bigBoard[i].getWinner();
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

            //cols
            colProducts[i%3] *= bigBoard[i].getWinner();
        }

        //cols cont
        for (int c : colProducts) {
            if (c == 8) {
                return 2;
            }
            else if (c == 1) {
                return 1;
            }
        }


        //diags
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

        return 0;
    }

    @Override
    public boolean isTerminal() {
        //if the winner isn't zero or if the board is full the game is terminal
        return (this.evaluateWinner() != 0) || this.isAllFull();
    }

    //returns the ultimate board
    public MiniBoard[] getBigBoard() {
        return bigBoard;
    }

    public MiniBoard getMiniBoard(int index) {
        return bigBoard[index];
    }

    //sums all the numbers of open 2 in a rows for a given player over all boards
    public int numberOfTwos(int player) {
        int num = 0;
        for (int i = 0; i < bigBoard.length; i ++) {
            num += bigBoard[i].numberTwos(player);
        }
        return num;
    }

    //sums the number of boards that a player has won so far
    public int numberOfThrees(int player) {
        int num = 0;
        for (int i = 0; i < bigBoard.length; i ++) {
            if (bigBoard[i].winner() == player) {
                num ++;
            }
        }
        return num;
    }

    //sums the number of open 2s that exist in the ultimate board
    // idea: maybe use a function that determines if a player can still possibly win a board to refine this
    // evaluation. the idea is that the winner could == 0 but there are cases where the player cannot win that
    // board so points should not be added in this case.
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

    //checks if all miniboards int the big board are full
    public boolean isAllFull() {
        for (int i = 0; i < bigBoard.length; i ++) {
            if (!bigBoard[i].isBoardFull()) {
                return false;
            }
        }
        return true;
    }

    protected void setBoardInPlay(int boardInPlay) {
        this.boardInPlay = boardInPlay;
    }

    //returns an integer representing the board in play, -1 means there are no restrictions
    public int getBoardInPlay() {
        return boardInPlay;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

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

    //updates the miniboards based on if someone has won them or not
    public void update() {
        for (int i = 0; i < bigBoard.length; i ++) {
            int win = bigBoard[i].winner();
            if (win != 0) {
                bigBoard[i].setWinner(win);
            }
        }
    }

    //wrapper to set a winner manually for one of the miniboards
    public void setBigWinner(int board, int player) {
        bigBoard[board].setWinner(player);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ultimate other = (Ultimate) obj;
        return Arrays.equals(bigBoard, other.bigBoard) && boardInPlay == other.boardInPlay;
    }
}
