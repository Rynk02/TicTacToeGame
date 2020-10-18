package ticTacToe;

import java.util.Scanner;
import java.util.Random;

public class TicCodeCLI {
	private static int choice1;
	private static int choice2;
	private static boolean playerOneWon = false;
	private static State[][] board = new State[3][3];
	private static String gameMode;
	
	static class Move {
		int row, col;
	}

	enum State {
		EMPTY(" "), X("X"), O("O");

		private String choice;

		State(String choice) {
			this.choice = choice;
		}

		public String getChoice() {
			return choice;
		}
	};

	public static void main(String[] args) {
		// initialize Board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = State.EMPTY;
			}
		}
		displayBoard(board);
		Scanner sc = new Scanner(System.in);
		System.out.println("\nMode(human, easy, medium, hard): ");
		gameMode = sc.next();
		while (!gameMode.equals("human") && !gameMode.equals("easy") && !gameMode.equals("medium") && !gameMode.equals("hard")) {			
			System.out.println("\nMode(human, easy, medium, hard): ");
			gameMode = sc.next();
			
		}
		// Starts the game
		while (true) {
			playerOneTurn(sc);
			displayBoard(board);
			if (checkBoard(board) || !isMovesLeft(board)) {
				break;
			}
			playerTwoTurn(sc);
			displayBoard(board);
			if (checkBoard(board) || !isMovesLeft(board)) {
				break;
			}
		}

		if (!isMovesLeft(board)) {
			System.out.println("\nIts a draw");
		}
		else if (playerOneWon) {
			System.out.println("\nPlayer one won!");
		}
		else if (!playerOneWon) {
			System.out.println("\nPlayer two won!");
		}
	}

	public static void playerOneTurn(Scanner sc) {
		boolean validChoice = false;
		while (!validChoice) {
			System.out.println("\nEnter a number to place a circle: ");
			choice1 = sc.nextInt();
			choice2 = sc.nextInt();
			try {
				if (board[choice1][choice2] == State.EMPTY) {
					board[choice1][choice2] = State.O;
					validChoice = true;
				}
				else {
					System.out.println("Invalid input");
				}
			}
			catch (Exception e) {
				System.out.println("Invalid input");
			}
		}
	}

	public static void playerTwoTurn(Scanner sc) {
		boolean validChoice = false;
		// human playing
		if (gameMode.equals("human")) {
			while (!validChoice) {
				System.out.println("\nEnter a number to place an X: ");
				choice1 = sc.nextInt();
				choice2 = sc.nextInt();
				try {
					if (board[choice1][choice2] == State.EMPTY) {
						board[choice1][choice2] = State.X;
						validChoice = true;
					}
					else {
						System.out.println("Invalid input");
					}
				}
				catch (Exception e) {
					System.out.println("Invalid Input");
				}
			}
		}

		// Computer easy which is random
		if (gameMode.equals("easy")) {
			Random rand = new Random();
			while (!validChoice) {
				choice1 = rand.nextInt(4);
				choice2 = rand.nextInt(4);
				try {
					if (board[choice1][choice2] == State.EMPTY) {
						board[choice1][choice2] = State.X;
						validChoice = true;
						System.out.println("\nComputer moved");
					}
					else {
						continue;
					}
				}
				catch (Exception e) {
					continue;
				}
			}
		}

		// Computer 50% to do a random move or a perfect move
		if (gameMode.equals("medium")) {
			Random rand = new Random();
			int ranNumber = rand.nextInt(1);
			if (ranNumber == 0) {
				while (!validChoice) {
					choice1 = rand.nextInt(4);
					choice2 = rand.nextInt(4);
					try {
						if (board[choice1][choice2] == State.EMPTY) {
							board[choice1][choice2] = State.X;
							validChoice = true;
							System.out.println("\nComputer moved");

						}
						else {
							continue;
						}
					}
					catch (Exception e) {
						continue;
					}
				}
			}
			else if (ranNumber == 1) {
				System.out.println("\nComputer Move");
				Move bestMove = findBestMove(board);
				board[bestMove.row][bestMove.col] = State.X;
			}
		}

		// 100% perfect move
		if (gameMode.equals("hard")) {
			System.out.println("\nComputer Move");
			Move bestMove = findBestMove(board);
			board[bestMove.row][bestMove.col] = State.X;
		}

	}

	public static boolean checkBoard(State[][] board) {
		// check row
		int count = 0;
		for (int row = 0; row < 3; row++) {
			count = 0;
			for (int col = 0; col < 3; col++) {
				if (board[row][col] == State.O) {
					count++;
				}
				else if (board[row][col] == State.X) {
					count--;
				}
				if (count == 3) {
					playerOneWon = true;
					return true;
				}
				else if (count == -3) {
					return true;
				}
			}
		}

		// check column
		for (int col = 0; col < 3; col++) {
			count = 0;
			for (int row = 0; row < 3; row++) {
				if (board[row][col] == State.O) {
					count++;
				}
				else if (board[row][col] == State.X) {
					count--;
				}
				if (count == 3) {
					playerOneWon = true;
					return true;
				}
				else if (count == -3) {
					return true;
				}
			}
		}

		// check diagonal
		count = 0;
		for (int col = 0; col < 3; col++) {
			if (board[col][col] == State.O) {
				count++;
			}
			else if (board[col][col] == State.X) {
				count--;
			}
		}
		if (count == 3) {
			playerOneWon = true;
			return true;
		}
		else if (count == -3) {
			return true;
		}

		// check diagonal
		count = 0;
		for (int col = 0; col < 3; col++) {
			if (board[col][2 - col] == State.O) {
				count++;
			}
			else if (board[col][2 - col] == State.X) {
				count--;
			}
		}
		if (count == 3) {
			playerOneWon = true;
			return true;
		}
		else if (count == -3) {

			return true;
		}

		return false;
	}

	public static Boolean isMovesLeft(State[][] board) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (board[i][j] == State.EMPTY)
					return true;
		return false;
	}

	public static int evaluate(State[][] b) {
		// Checking for Rows for X or O victory.
		for (int row = 0; row < 3; row++) {
			if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
				if (b[row][0] == State.X)
					return +10;
				else if (b[row][0] == State.O)
					return -10;
			}
		}

		// Checking for Columns for X or O victory.
		for (int col = 0; col < 3; col++) {
			if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
				if (b[0][col] == State.X)
					return +10;

				else if (b[0][col] == State.O)
					return -10;
			}
		}

		// Checking for Diagonals for X or O victory.
		if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
			if (b[0][0] == State.X)
				return +10;
			else if (b[0][0] == State.O)
				return -10;
		}

		if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
			if (b[0][2] == State.X)
				return +10;
			else if (b[0][2] == State.O)
				return -10;
		}

		// Else if none of them have won then return 0
		return 0;
	}

	//This is the minimax function. It considers all 
	//the possible ways the game can go and returns 
	//the value of the board 
	public static int minimax(State[][] board, int depth, Boolean isMax) {
		int score = evaluate(board);

		// If Maximizer has won the game
		// return his/her evaluated score
		if (score == 10)
			return score;

		// If Minimizer has won the game
		// return his/her evaluated score
		if (score == -10)
			return score;

		// If there are no more moves and
		// no winner then it is a tie
		if (isMovesLeft(board) == false)
			return 0;

		// If this maximizer's move
		if (isMax) {
			int best = -1000;

			// Traverse all cells
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// Check if cell is empty
					if (board[i][j] == State.EMPTY) {
						// Make the move
						board[i][j] = State.X;

						// Call minimax recursively and choose
						// the maximum value
						best = Math.max(best, minimax(board, depth + 1, !isMax));

						// Undo the move
						board[i][j] = State.EMPTY;
					}
				}
			}
			return best;
		}

		// If this minimizer's move
		else {
			int best = 1000;

			// Traverse all cells
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// Check if cell is empty
					if (board[i][j] == State.EMPTY) {
						// Make the move
						board[i][j] = State.O;

						// Call minimax recursively and choose
						// the minimum value
						best = Math.min(best, minimax(board, depth + 1, !isMax));

						// Undo the move
						board[i][j] = State.EMPTY;
					}
				}
			}
			
			return best;
		}
	}

	//This will return the best possible 
	//move for the player 
	public static Move findBestMove(State[][] board) {
		int bestVal = -1000;
		Move bestMove = new Move();
		bestMove.row = -1;
		bestMove.col = -1;

		// Traverse all cells, evaluate minimax function
		// for all empty cells. And return the cell
		// with optimal value.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// Check if cell is empty
				if (board[i][j] == State.EMPTY) {
					// Make the move
					board[i][j] = State.X;

					// compute evaluation function for this
					// move.
					int moveVal = minimax(board, 0, false);

					// Undo the move
					board[i][j] = State.EMPTY;

					// If the value of the current move is
					// more than the best value, then update
					// best/
					if (moveVal > bestVal) {
						bestMove.row = i;
						bestMove.col = j;
						bestVal = moveVal;
					}
				}
			}
		}
		return bestMove;
	}
	//display the game state
	public static void displayBoard(State[][] board) {
		String seperator = "------------";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(" " + board[i][j].getChoice() + " |");
			}
			if (i == 2) {
				break;
			}
			System.out.println("\n" + seperator);
		}
	}

}
