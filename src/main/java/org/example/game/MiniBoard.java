package org.example.game;
import java.util.*;

public class MiniBoard {

    private int[] board = new int[9];
    private boolean isFull = false;
    private int winner = 0; // 1 = O and 2 = X (DON'T FORGET THIS!!!)

    public static void main(String[] args) {

    }

    public MiniBoard() {

    }

    public MiniBoard(MiniBoard t, int row, int col, int player) {
        board = t.getBoard();
        winner = t.getWinner();
        isFull = t.isBoardFull();

        this.place(row, col, player);
    }

    protected MiniBoard(int[] board) { //used for testing
        this.board = board;
    }

    public MiniBoard place(int row, int col, int player) {
        if (board[row * 3 + col] == 0) {
            board[row * 3 + col] = player;
        } else {
            throw new UnsupportedOperationException("There is already a piece in that square.");
        }
        return this;
    }

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

    //returns 0 for tie, 1 for player 1, and 2 for player 2
    public int winner() {

        int rowProduct = 1;
        int[] colProducts = {1, 1, 1};
        int[] diagProducts = {1, 1};


        for (int i = 0; i < 9; i++) {

            //rows
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

            //cols
            colProducts[i % 3] *= board[i];
        }

        //cols cont
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


        //diags
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

    public void setWinner(int winningPlayer) {

        isFull = true;
        winner = winningPlayer;

        for (int i = 0; i < board.length; i++) {
            board[i] = winningPlayer;
        }
    }

    public boolean isBoardFull() {
        if (isFull) {
            return isFull;
        } else {
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    public int[] getBoard() {
        return board;
    }

    public int getWinner() {
        return winner;
    }

    public void printOneLine(int line) {
        for (int i = 0; i < board.length; i++) {
            if (i / 3 == line) {
                System.out.print(playerToChar(board[i]));
            }
        }
    }

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

    public char playerToChar(int player) {
        if (player == 1) {
            return 'O';
        }
        if (player == 2) {
            return 'X';
        }
        return ' ';
    }

    public boolean isBlank() {
        for (int i = 0; i < board.length; i ++) {
            if (board[i] != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(board);
        result = prime * result + Objects.hash(isFull, winner);
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
        MiniBoard other = (MiniBoard) obj;
        return Arrays.equals(board, other.board) && isFull == other.isFull && winner == other.winner;
    }

}