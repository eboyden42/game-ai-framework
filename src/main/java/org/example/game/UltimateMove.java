package org.example.game;

import org.example.game.Ultimate;

public class UltimateMove {
    /**
     * POJO for storing information about a move on an Ultimate tic-tac-toe board.
     *
     * @author Eli Boyden, eboyden42
     */

    private int index;
    private int row;
    private int col;

    public UltimateMove(int index, int row, int col) {
        this.index = index;
        this.row = row;
        this.col = col;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "UltimateMove{" +
                "index=" + index +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
