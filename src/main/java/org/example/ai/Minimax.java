package org.example.ai;

import org.example.game.Ultimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Minimax implements CPU<Ultimate> {

    private int player;
    private ArrayList<Integer> cpuEvaluationsPerMove = new ArrayList<>();

    private int tableUses = 0;
    private HashMap<Integer, Entry> tt = new HashMap<Integer, Entry>();
    private Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Ultimate dad = new Ultimate();
        ArrayList<Ultimate> children = dad.generateMoves(1);
        Minimax m = new Minimax(1);

        for (int i = 0; i < children.size(); i ++) {
            children.get(i).print();
            System.out.println(m.evaluate(children.get(i)));
        }


        Ultimate u = new Ultimate().place(0, 0, 0, 1).place(0, 1, 0, 2).place(3, 1, 0, 1);

        u.print();

        double duration = 5.0;
        double time = 0.0;
        int depth = 0;
        int bestScore = -1000000;

        long start = System.currentTimeMillis();
        while (time < duration) {

            bestScore = m.alphabetaTT(u, depth, -100000, 100000, true);

            time += ((System.currentTimeMillis() - start)/1000.0);
            System.out.printf("Depth: %d | Time: %f | Score: %d\n", depth, time, bestScore);
            depth ++;
        }
    }

    public Minimax(int player) {
        this.player = player;
    }

    public int getTableUses() {
        return tableUses;
    }

    public void clearTableUses() {
        tableUses = 0;
    }

    public void clearTT() {
        tt.clear();
        tableUses = 0;
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
            int score = this.minimax(a.get(i), depth, false);
            if (score > highestScore) {
                highestScore = score;
                highestIndex = i;
            }
        }
        cpuEvaluationsPerMove.add(highestScore);
        return a.get(highestIndex);
    }


    public int minimax(Ultimate node, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || node.winner() != 0 || node.isAllFull()) {
            return evaluate(node);
        }
        if (isMaximizingPlayer) {
            int value = -100000;
            ArrayList<Ultimate> a = node.generateMoves(player);
            for (int i = 0; i < a.size(); i ++) {
                value = Math.max(value, minimax(a.get(i), depth-1, false));
            }
            return value;
        }
        else {
            int value = 100000;
            ArrayList<Ultimate> a = node.generateMoves(-player + 3);
            for (int i = 0; i < a.size(); i ++) {
                value = Math.min(value, minimax(a.get(i), depth-1, true));
            }
            return value;
        }
    }

    public int negamax(Ultimate node, int depth, int color) { //SOMETHING IS WRONG WITH THIS SHIT
        if (depth == 0 || node.winner() != 0 || node.isAllFull()) {
            return color*evaluate(node);
        }
        int value = -100000;
        ArrayList<Ultimate> a = node.generateMoves(player);
        for (int i = 0; i < a.size(); i ++) {
            value = Math.max(value, -negamax(a.get(i), depth-1, -color));
        }
        return value;
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

    public int alphabetaTT(Ultimate node, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || node.winner() != 0 || node.isAllFull()) {
            return evaluate(node);
        }

        int hashKey = node.hashCode();
        Entry entry = tt.get(hashKey);

        if (tt.containsKey(hashKey) && entry.getDepth() >= depth) {
            tableUses ++;
            if (entry.getFlag().equals("EXACT")) {
                return entry.getValue();
            }
            else if (entry.getFlag().equals("LOWERBOUND")) {
                alpha = Math.max(alpha, entry.getValue());
            }
            else if (entry.getFlag().equals("UPPERBOUND")) {
                beta = Math.min(beta, entry.getValue());
            }
        }

        if (isMaximizingPlayer) {
            int value = -100000;
            ArrayList<Ultimate> a = node.generateMoves(player);
            for (int i = 0; i < a.size(); i ++) {
                value = Math.max(value, alphabetaTT(a.get(i), depth-1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }

            if (value <= alpha) {
                tt.put(hashKey, new Entry(value, depth, "UPPERBOUND"));
            }
            else if (value >= beta) {
                tt.put(hashKey, new Entry(value, depth, "LOWERBOUND"));
            }
            else {
                tt.put(hashKey, new Entry(value, depth, "EXACT"));
            }

            return value;
        }
        else {
            int value = 100000;
            ArrayList<Ultimate> a = node.generateMoves(-player + 3);
            for (int i = 0; i < a.size(); i ++) {
                value = Math.min(value, alphabetaTT(a.get(i), depth-1, alpha, beta, true));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }

            if (value <= alpha) {
                tt.put(hashKey, new Entry(value, depth, "UPPERBOUND"));
            }
            else if (value >= beta) {
                tt.put(hashKey, new Entry(value, depth, "LOWERBOUND"));
            }
            else {
                tt.put(hashKey, new Entry(value, depth, "EXACT"));
            }

            return value;
        }
    }

    public int MTD(Ultimate root, int f, int depth) {
        int g = 0;
        int upperbound = 100000;
        int lowerbound = -100000;
        int beta = 10000000;

        while (lowerbound < upperbound) {
            if (g == lowerbound) {
                beta = g + 1;
            }
            else {
                beta = g;
            }

            g = alphabetaTT(root, depth, beta - f, beta + f, false);

            if (g < beta) {
                upperbound = g;
            }
            else {
                lowerbound = g;
            }
        }

        return g;
    }

    public int getLatestEvaluation() {
        return cpuEvaluationsPerMove.get(cpuEvaluationsPerMove.size()-1);
    }
}
