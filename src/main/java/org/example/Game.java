package org.example;

import org.example.ai.*;
import org.example.game.Ultimate;

import java.util.Scanner;

public class Game {

    //true = player; false = AI
    private boolean player1;
    private boolean player2;
    private Ultimate gameBoard = new Ultimate();
    private CPU<Ultimate> bonnie = new AlphaBeta(1);
    private CPU<Ultimate> clyde = new AlphaBeta(2);

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

        int depth = 8;
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
                long start = System.currentTimeMillis();
                gameBoard = bonnie.search(gameBoard, depth, 1);
                long time = System.currentTimeMillis() - start;
                System.out.printf("Player 1 AI Time: %f | Evaluated Board Score: %d\n", time/1000.0, bonnie.getLatestEvaluation());
                System.out.printf("Move: %d\n", move);
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
                long start = System.currentTimeMillis();
                gameBoard = clyde.search(gameBoard, depth, 2);
                long time = System.currentTimeMillis() - start;
                System.out.printf("Player 2 AI Time: %f | Evaluated Board Score: %d\n", time/1000.0, clyde.getLatestEvaluation());
                if (!player1) {
                    gameBoard.print();
                }
            }
        }
        System.out.println("|||||||||||||||||||||||||||");
        gameBoard.print();
        System.out.printf("WINNER: Player %d\n", gameBoard.winner());
    }
}
