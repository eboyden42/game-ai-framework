package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    //true = player; false = AI
    private boolean player1;
    private boolean player2;
    private Ultimate gameBoard = new Ultimate();
    private Minimax bonnie = new Minimax(1);
    private Minimax clyde = new Minimax(2);

    public static void main(String[] args) {
        Game g = new Game(true, false);
        g.play();
    }

    public Game() {
        player1 = true;
        player2 = true;
    }

    public Game(boolean player1) {
        this.player1 = player1;
        player2 = true;
    }

    public Game(boolean player1, boolean player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void play() {

        int depth = 7;
        Scanner scan = new Scanner(System.in);
        int move = 0;

        while (gameBoard.winner() == 0 && !gameBoard.isAllFull()) {
            //System.out.println("Next Move?");
            //scan.next();
            move ++;

            if (player1) {
                //if player1 is human
                System.out.printf("Move: %d\n", move);
                gameBoard.print();
                System.out.print("Board of Play: ");
                System.out.println(gameBoard.getBoardInPlay());
                boolean invalidInput = true;
                int board = gameBoard.getBoardInPlay();
                int row = 0;
                int col = 0;
                while(invalidInput) {
                    if (gameBoard.getBoardInPlay() == -1) {
                        System.out.println("Enter Board Number: ");
                        board = scan.nextInt();
                    }
                    System.out.println("Enter Row Number: ");
                    row = scan.nextInt();
                    System.out.println("Enter Col Number: ");
                    col = scan.nextInt();
                    invalidInput = !(gameBoard.isMovePossible(board, row, col));
                }
                gameBoard.place(board, row, col, 1);

                if (gameBoard.winner() == 1 || gameBoard.isAllFull()) {
                    break;
                }
            }
            else {
                //if player1 is an AI
                ArrayList<Ultimate> a = gameBoard.generateMoves(1);
                bonnie.clearTableUses();
                int highestIndex = 0;
                int highestScore = -100000;
                long start = System.currentTimeMillis();
                for (int i = 0; i < a.size(); i ++) {
                    int score = bonnie.alphabetaTT(a.get(i), depth, -100000, 100000, false);
                    //System.out.printf("Player 1 Table Uses: %d\n",bonnie.getTableUses());
                    if (score > highestScore) {
                        highestScore = score;
                        highestIndex = i;
                    }
                }
                long time = System.currentTimeMillis() - start;
                System.out.printf("Player 1 Time: %f\n", time/1000.0);
                System.out.printf("Move: %d\n", move);
                System.out.printf("Player 1 AI: %d\n", highestScore);
                gameBoard = a.get(highestIndex);
                gameBoard.print();

                if (gameBoard.winner() == 1 || gameBoard.isAllFull()) {
                    break;
                }
            }

            if (player2) {
                //if player2 is human
                System.out.print("Board of Play: ");
                System.out.println(gameBoard.getBoardInPlay());
                boolean invalidInput = true;
                int board = gameBoard.getBoardInPlay();
                int row = 0;
                int col = 0;
                while(invalidInput) {
                    if (gameBoard.getBoardInPlay() == -1) {
                        System.out.println("Enter Board Number: ");
                        board = scan.nextInt();
                    }
                    System.out.println("Enter Row Number: ");
                    row = scan.nextInt();
                    System.out.println("Enter Col Number: ");
                    col = scan.nextInt();
                    invalidInput = !(gameBoard.isMovePossible(board, row, col));
                }
                gameBoard.place(board, row, col, 2);
            }
            else {
                //if player2 is AI
                ArrayList<Ultimate> a = gameBoard.generateMoves(2);
                clyde.clearTableUses();
                int highestIndex = 0;
                int highestScore = -100000;
                for (int i = 0; i < a.size(); i ++) {
                    int score = clyde.MTD(a.get(i), clyde.alphabetaTT(a.get(i), 5, -100000, 100000, false), 20);
                    if (score > highestScore) {
                        highestScore = score;
                        highestIndex = i;
                    }
                    //System.out.printf("Player 2 Table Uses: %d\n", clyde.getTableUses());
                }
                System.out.printf("Player 2 AI: %d\n", highestScore);
                gameBoard = a.get(highestIndex);
            }
        }
        System.out.println("|||||||||||||||||||||||||||");
        gameBoard.print();
        System.out.printf("WINNER: %d\n", gameBoard.winner());
    }
}
