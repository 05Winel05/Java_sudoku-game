# Sudoku Game (Java)

This is a Sudoku game I created in Java as a university project. It has a graphical interface where you can play a 9×9 puzzle, check your answers, or let the computer solve it.

## Features

- Generate a new Sudoku puzzle.
- Fill in empty cells using the number buttons or your keyboard.
- Check your answers with color feedback.
- Solve the puzzle automatically using a backtracking algorithm.
- Generate puzzles with exactly one solution.

## Screenshot

![Sudoku game interface](screenshots/sudoku-game.png)

## Technologies

- Java
- Java Swing for the graphical interface
- Backtracking for solving and verifying puzzles

## How to run

You need a Java Development Kit (JDK), version 8 or newer.

1. Download or clone this repository.
2. Open a terminal in the folder containing the `.java` files.
3. Compile and run the game:

```bash
javac *.java
java Main
```

A desktop window is needed because this is a Swing application.

## How to play

1. Open the application. A puzzle appears automatically.
2. Click an empty cell and choose a number from 1 to 9, or type it with your keyboard.
3. Click **Check Solution** to see which answers are correct.
4. Click **Solve** to reveal the completed puzzle.
5. Click **Generate New** to start another game.

## How it works

`SudokuGenerator` creates a valid complete board and removes numbers while making sure the puzzle has one solution. `SudokuSolver` uses backtracking to solve the board and count solutions. `SudokuGame` handles the Swing interface, buttons, and answer checking. `Main` starts the application.

## What I learned

Working on this project helped me practice Java classes, a graphical user interface, and using algorithms to solve a problem.

---

Created by Elmyrza Zhenishbekov.
