package org.example.game;

import org.example.game.Ultimate;

public class UltimateMove {
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
