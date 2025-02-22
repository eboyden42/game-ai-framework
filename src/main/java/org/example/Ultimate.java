package org.example;

import org.example.ai.Minimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Ultimate implements Comparable<Ultimate> {

    private MiniBoard[] bigBoard = new MiniBoard[9];
    private int boardInPlay;
    private int storedValue;

    public Ultimate() {
        boardInPlay = -1;
        for (int i = 0; i < bigBoard.length; i ++) {
            bigBoard[i] = new MiniBoard();
        }
    }

    public Ultimate(MiniBoard[] bigBoard2, int index, int row, int col, int player) {
        boardInPlay = -1;
        for (int i = 0; i < bigBoard.length; i ++) {
            bigBoard[i] = new MiniBoard();
        }
        for (int i = 0; i < bigBoard.length; i ++) {
            for (int n = 0; n < bigBoard[0].getBoard().length; n ++) {
                bigBoard[i].getBoard()[n] = bigBoard2[i].getBoard()[n];
            }
        }
        this.place(index, row, col, player);
    }


    //sets natural order to the greatest static evaluation with positive being player 1
    public int compareTo(Ultimate other) {
        int positivePlayer = 1;
        return this.evaluate(positivePlayer)-other.evaluate(positivePlayer);
    }

    public int evaluate(int player) {
        Minimax m = new Minimax(player);
        return m.evaluate(this);
    }

    public static void main(String[] args) {
        Ultimate u1 = new Ultimate();
        Ultimate u2 = new Ultimate();

        u1.setBigWinner(0, 1);
        u1.setBigWinner(1, 1);
        u1.setBigWinner(2, 2);
        u1.setBigWinner(3, 2);
        u1.setBigWinner(4, 2);
        u1.setBigWinner(5, 1);
        u1.setBigWinner(6, 1);
        u1.setBigWinner(7, 1);
        u1.setBigWinner(8, 2);

        u1.print();
        System.out.println(u1.winner());
    }

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

    public Ultimate place(int bigIndex, int row, int col, int player) {

        bigBoard[bigIndex].place(row, col, player);
        this.update();

        if (bigBoard[row*3+col].isBoardFull()){
            boardInPlay = -1;
        }
        else {
            boardInPlay = row*3+col;
        }

        return this;
    }

    public boolean isMovePossible(int index, int row, int col) {
        if (index == boardInPlay || boardInPlay == -1) {
            if (bigBoard[index].getBoard()[row*3+col] == 0) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Ultimate> generateMoves(int player) {
        ArrayList<Ultimate> moves = new ArrayList<Ultimate>();

        //if any board can be played in, check them all
        if (boardInPlay == -1) {
            for (int index = 0; index < bigBoard.length; index ++) {
                //if the board is full or there is a winner for it, do nothing
                if (!bigBoard[index].isBoardFull() && (bigBoard[index].getWinner() == 0)) {
                    for (int row = 0; row < 3; row ++) {
                        for (int col = 0; col < 3; col ++) {
                            if (this.isMovePossible(index, row, col)) {
                                moves.add(new Ultimate(this.getBigBoard(), index, row, col, player));
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
                        moves.add(new Ultimate(this.getBigBoard(), boardInPlay, row, col, player));
                    }
                }
            }
        }

        return moves;
    }

    public int winner() {
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

    public MiniBoard[] getBigBoard() {
        return bigBoard;
    }

    public int numberOfTwos(int player) {
        int num = 0;
        for (int i = 0; i < bigBoard.length; i ++) {
            num += bigBoard[i].numberTwos(player);
        }
        return num;
    }

    public int numberOfThrees(int player) {
        int num = 0;
        for (int i = 0; i < bigBoard.length; i ++) {
            if (bigBoard[i].winner() == player) {
                num ++;
            }
        }
        return num;
    }

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

    public boolean isAllFull() {
        for (int i = 0; i < bigBoard.length; i ++) {
            if (!bigBoard[i].isBoardFull()) {
                return false;
            }
        }
        return true;
    }

    public int getStoredValue() {
        return storedValue;
    }

    public void setStoredValue(int storedValue) {
        this.storedValue = storedValue;
    }

    public int getBoardInPlay() {
        return boardInPlay;
    }

    public void update() {
        for (int i = 0; i < bigBoard.length; i ++) {
            int win = bigBoard[i].winner();
            if (win != 0) {
                bigBoard[i].setWinner(win);
            }
        }
    }

    public void setBigWinner(int board, int player) {
        bigBoard[board].setWinner(player);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bigBoard);
        result = prime * result + Objects.hash(boardInPlay, storedValue);
        return result;
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
        return Arrays.equals(bigBoard, other.bigBoard) && boardInPlay == other.boardInPlay
                && storedValue == other.storedValue;
    }
}
