package org.example.ai;

public interface CPU<T> {

    // Static evaluation function for a game given game state
    public int evaluate(T state);

    // Use evaluation function and tree search methods to output a game state with the maximal move made
    // player refers to the player that will be making the move, game state inputs should therefore be the state
    // before the player moves
    public T search(T state, int depth, int player);

    public int getLatestEvaluation();
}
