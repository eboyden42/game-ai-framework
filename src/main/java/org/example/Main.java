package org.example;

public class Main {
    public static void main(String[] args) {
        //PLAYER VARIABLES
        final boolean PLAYER_ONE_IS_REAL = false;
        final boolean PLAYER_TWO_IS_REAL = false;

        Game g = new Game(PLAYER_ONE_IS_REAL, PLAYER_TWO_IS_REAL);
        g.play();
    }
}