# TicTacToe
  This game is a 4 * 4 Tic Tac Toe game. The player places ‘O’ and AI places ‘X’. In the hard difficulty, after player’s turn, AI uses minimax algorithm with alpha-beta pruning and evaluation function to determine its best move. A bunch of information will be printed during the algorithm. UI is designed for this game.

## How to compile and run: 

Command line:

        ／*enter src folder*／


        javac game/Main.java


        java game.Main

## Classes
1. Main.java: UI that shows the grids on the screen and allow users to use the mouse to place symbols

2. TicTacToe.java: the inner logic of a tic tac toe game. 

3. AIPlayer.java: a super class representing an AI to read the board and determine its move.

4. HardAI.java: AI with hard difficulty. It uses alpha-beta pruning to determine its move, and when the program can’t search to the maximum depth, I cut off the search at a certain depth using an evaluation function for non-terminal nodes.

5. MedAI.java: AI with intermediate difficulty. It also uses alpha-beta pruning, but when reaching depth 2, we cut off the search and the utility value is 0 if it is not a terminal node.

6. EasyAI.java: AI with easy difficulty. It uses alpha-beta pruning but choose the worst move. When it reaches a certain depth, it directly returns a value.
