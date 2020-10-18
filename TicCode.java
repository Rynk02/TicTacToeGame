package ticTacToe;

import javax.swing.JFrame;

import ticTacToe.TicCodeCLI.State;

import java.util.Random;

public class TicCode {
	private State[][] board;
	private String gameMode;
	private boolean playerOneWon;
	public TicCode() {
		gameMode = "";
		board = new State[3][3];
		playerOneWon = false;
	}

	public class Move {
		int row, col;
	}

	public enum State {
		EMPTY(" "), X("X"), O("O");

		private String choice;

		State(String choice) {
			this.choice = choice;
		}

		public String getChoice() {
			return choice;
		}
	};

	public void startGame() {
		startGUI();
	}

	public Move Computer() {
		Move move = new Move();
		Random rand = new Random();
		move.row = 0;
		move.col = 0;
		if (getGameMode().equals("easy")) {
			while (true) {
				move.row = rand.nextInt(3);
				move.col = rand.nextInt(3);
				if (board[move.row][move.col] == State.EMPTY) {
					board[move.row][move.col] = State.X;
					break;
				}
				else {
					continue;
				}
			}
		}
		// Computer 50% to do a random move or a perfect move
		if (getGameMode().equals("medium")) {
			int ranNumber = rand.nextInt(1);
			if (ranNumber == 0) {
				while (true) {
					move.row = rand.nextInt(3);
					move.col = rand.nextInt(3);
					if (board[move.row][move.col] == State.EMPTY) {
						board[move.row][move.col] = State.X;
						break;
					}
					else {
						continue;
					}
				}
			}
			else if (ranNumber == 1) {
				Move bestMove = findBestMove(getBoard());
				board[bestMove.row][bestMove.col] = State.X;
				return bestMove;
			}
		}

		// 100% perfect move
		if (getGameMode().equals("hard")) {
			Move bestMove = findBestMove(getBoard());
			board[bestMove.row][bestMove.col] = State.X;
			return bestMove;
		}

		return move;
	}

	public boolean checkBoard(State[][] board) {
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
	
	// This function returns true if there are moves 
	// remaining on the board. It returns false if 
	// there are no moves left to play.
	public Boolean isMovesLeft(State[][] board) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (board[i][j] == State.EMPTY)
					return true;
		return false;
	}

	// This is the evaluation function as discussed
	// in the previous article ( http://goo.gl/sJgv68 )
	public int evaluate(State[][] b) {
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

	// This is the minimax function. It considers all
	// the possible ways the game can go and returns
	// the value of the board
	public int minimax(State[][] board, int depth, Boolean isMax) {
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

	// This will return the best possible
	// move for the player
	public Move findBestMove(State[][] board) {
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

	public void startGUI() {
		JFrame window = new JFrame("Tic-Tac-Toe");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().add(new TicGUI());
		window.setBounds(300, 200, 300, 300);
		window.setVisible(true);
	}

	public State[][] getBoard() {
		return board;
	}

	public void resetBoard(State[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = State.EMPTY;
			}
		}
	}

	public void changeBoard(State[][] board, int row, int column, boolean playerOne) {
		if (playerOne) {
			board[row][column] = State.O;
		}
		else {
			board[row][column] = State.X;
		}
	}

	public String getGameMode() {
		return gameMode;
	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}
	public void printStates(State[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				System.out.println(board[i][j]);
			}
		}
	}

	public boolean isPlayerOneWon() {
		return playerOneWon;
	}

	public void setPlayerOneWon(boolean playerOneWon) {
		this.playerOneWon = playerOneWon;
	}

}
