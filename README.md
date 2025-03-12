# Create Your Own Game AI

## Description
This project provides a framework for programmers interested in implementing AI algorithms for two-player games. It defines two key interfaces:

- **`GameState<M>`** – Represents the state of a game.
- **`SearchAlgorithm<M>`** – Represents an AI algorithm that selects moves.

Additionally, the **`Play`** class enables running a game using a specified AI search algorithm.

The framework includes several built-in AI algorithms (see `org.example.ai`) and an implementation of **Ultimate Tic-Tac-Toe** (`org.example.game`). Unit tests accompany these implementations, and adding your own tests is highly recommended when developing new games or algorithms.

> **Note:** I'm considering teaching a project walkthrough using this framework as a class at UVA next semester.

## Installation & Setup

### Running Ultimate Tic-Tac-Toe
To play **Ultimate Tic-Tac-Toe** using the provided AI, simply download and run the JAR file:

```sh
java -jar Ultimate.jar
