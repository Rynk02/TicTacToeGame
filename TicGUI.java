package ticTacToe;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import ticTacToe.TicCode.*;
import java.util.Random;
public class TicGUI extends JPanel {
	JButton buttons[][] = new JButton[3][3];
	int alternate = 0;// if this number is a even, then put a X. If it's odd, then put an O
	TicCode code;

	public TicGUI() {
		setLayout(new GridLayout(3, 3));
		code = new TicCode();
		code.resetBoard(code.getBoard());
		playGame();
		initializebuttons();
	}
	public void playGame() {
		Object[] options = {"Human", "Easy", "Medium", "Hard"};
		int n = JOptionPane.showOptionDialog(null, "Choose your game option", "Welcome to Tic Tac Toe", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		switch (n) {
		case 0:
			code.setGameMode("human");
			break;
		case 1:
			code.setGameMode("easy");
			break;
		case 2:
			code.setGameMode("medium");
			break;
		case 3:
			code.setGameMode("hard");
		}
	}
	
	public void initializebuttons() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setText(State.EMPTY.getChoice());
				buttons[i][j].addActionListener(new buttonListener());
				
				add(buttons[i][j]); // adds this button to JPanel (note: no need for JPanel.add(...)
				// because this whole class is a JPanel already

			}
		}
	}

	public void resetButtons() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j].setText(State.EMPTY.getChoice());
			}
		}
	}

// when a button is clicked, it generates an ActionEvent. Thus, each button needs an ActionListener. When it is clicked, it goes to this listener class that I have created and goes to the actionPerformed method. There (and in this class), we decide what we want to do.
	private class buttonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JButton buttonClicked = (JButton) e.getSource();// get the particular button that was clicked
			if (code.getGameMode() != "human") {
				for (int row = 0; row < 3; row++) {
					for (int col = 0; col < 3; col++) {
						if (buttonClicked.equals(buttons[row][col])) {
							code.changeBoard(code.getBoard(), row, col, true);
							buttonClicked.setText(State.O.getChoice());
						}
					}
				}
				if (code.isMovesLeft(code.getBoard())) {
					Move move = code.Computer();
					buttons[move.row][move.col].setText(State.X.getChoice());
				}
			}
			else {
				if (alternate % 2 == 0) {
					for (int row = 0; row < 3; row++) {
						for (int col = 0; col < 3; col++) {
							if (buttonClicked.equals(buttons[row][col])) {
								code.changeBoard(code.getBoard(), row, col, true);
								buttonClicked.setText(State.O.getChoice());
								code.printStates(code.getBoard());
							}
						}
					}
				}
				else {
					for (int row = 0; row < 3; row++) {
						for (int col = 0; col < 3; col++) {
							if (buttonClicked.equals(buttons[row][col])) {
								code.changeBoard(code.getBoard(), row, col, false);
								buttonClicked.setText(State.X.getChoice());
								code.printStates(code.getBoard());
							}
						}
					}		
				}
			}
			
			if (code.checkBoard(code.getBoard()) || !code.isMovesLeft(code.getBoard())) {
				Object[] options = {"Play Again", "Quit"};
				String winner = "";
				if (!code.isMovesLeft(code.getBoard())){
					winner = "It was a draw!";
				}
				else if (code.isPlayerOneWon()) {
					winner = "Player One Wins!";
				}
				else {
					winner = "Player Two Wins!";
				}
				int n = JOptionPane.showOptionDialog(null, "Would you like to play again?", winner, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (n == 0) {

					resetButtons();
					code.resetBoard(code.getBoard());
					playGame();
				}
				else {
					System.exit(0);
				}
			}
			alternate++;

		}
	}
}
