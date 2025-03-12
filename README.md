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

## Using the Framework for Your Own Game AI  

### 1. Implement the `GameState` Interface  
- Choose a game you want to create an AI for.  
- Refer to the documentation and existing implementations for guidance.  
- **Using `Ultimate.java` as a template** may help, but your game will likely have unique mechanics.  

### 2. Implement an AI Algorithm (Optional)  
- You can implement your own `SearchAlgorithm`, or use one of the existing ones.  

### 3. Write Unit Tests  
- Ensure correctness by writing **unit tests** for your game and AI implementations.  

### 4. Modify `org.example.Main`  
- Update `org.example.Main` to pass your game and AI classes to the `Play` constructor.  

### 5. Run and Play  
- Execute the program directly or **build and run using Gradle** to play against your AI!  

## Usage Examples  
- Implement AI for **Connect-4, Checkers, Mancala, Lines and Boxes,** or even **Chess** (*challenging but fun!*)  
- Experiment with different AI algorithms to compare their efficiency and decision-making.  

## Dependencies  
This project uses:  
- **Gradle** for building  
- **Maven** for dependency management  
- **JUnit** and **Mockito** for unit testing  

## Contributing  
Contributions are welcome! If you'd like to add new AI algorithms, game implementations, or improvements to the framework, feel free to submit a pull request.  