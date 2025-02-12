package org.example;

public class Main {
    public static void main(String[] args) {
        MiniBoard b = new MiniBoard();
        b.print();
        b.place(1, 2, 1);
        b.print();
    }
}