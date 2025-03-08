package org.example.ai;

import org.example.game.Ultimate;

import java.util.ArrayList;

public class AlphaBeta implements CPU<Ultimate> {
    private final int player;
    //stores the score that was recorded for each move in the game
    private ArrayList<Integer> cpuEvaluationsPerMove = new ArrayList<>();

    public AlphaBeta(int player) {
        this.player = player;
    }

    public int evaluate(Ultimate board) {
        int eval = 0;

        if (board.winner() != 0) {
            if (board.winner() == player) {
                return 10000;
            }
            else {
                return -10000;
            }
        }

        int twoMultiplier = 20;

        eval += board.numberOfTwos(player) * twoMultiplier;
        eval -= board.numberOfTwos(-player + 3) * twoMultiplier;

        int threeMultiplier = 50;

        eval += board.numberOfThrees(player) * threeMultiplier;
        eval -= board.numberOfThrees(-player + 3) * threeMultiplier;

        int bigTwoMultiplier = 100;

        eval += board.numberOfBigTwos(player) * bigTwoMultiplier;
        eval -= board.numberOfBigTwos(-player + 3) * bigTwoMultiplier;

        return eval;
    }

    public Ultimate search(Ultimate state, int depth, int player) {
        ArrayList<Ultimate> a = state.generateMoves(player);
        int highestIndex = 0;
        int highestScore = -1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < a.size(); i ++) {
            int score = this.alphabeta(a.get(i), depth, -100000, 100000,false);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        cpuEvaluationsPerMove.add(highestScore);
        return a.get(highestIndex);
    }

    public int alphabeta(Ultimate node, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || node.winner() != 0 || node.isAllFull()) {
            return evaluate(node);
        }
        if (isMaximizingPlayer) {
            int value = -100000;
            ArrayList<Ultimate> a = node.generateMoves(player);
            for (int i = 0; i < a.size(); i ++) {
                value = Math.max(value, alphabeta(a.get(i), depth-1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        }
        else {
            int value = 100000;
            ArrayList<Ultimate> a = node.generateMoves(-player + 3);
            for (int i = 0; i < a.size(); i ++) {
                value = Math.min(value, alphabeta(a.get(i), depth-1, alpha, beta, true));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }

    public ArrayList<Integer> getCpuEvaluationsPerMove() {
        return cpuEvaluationsPerMove;
    }

    public int getLatestEvaluation() {
        return cpuEvaluationsPerMove.get(cpuEvaluationsPerMove.size()-1);
    }
}
