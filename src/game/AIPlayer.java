package game;

import java.util.Random;

public abstract class AIPlayer {
	
	TicTacToe ttt;
	
	//dimension of TicTacToe
	int dimension = TicTacToe.dimension;
	
	char aiMark = TicTacToe.aiMark;
	
	char playerMark = TicTacToe.playerMark;
	
	//the index of rows and columns	
	int[] rows, cols;
	
	//current count of ttt when start running aiPut
	int currcount;
	
	//current board of ttt when start running aiPut
	char[][] board;
	
	//certain depth level
	static final int level = 9;
	
	public AIPlayer() {
		this.board = new char[dimension][dimension];	
		rows = new int[]{0, 1, 2, 3};
		cols = new int[]{0, 1, 2, 3};
		shuffle(rows);
		shuffle(cols);	
	}
	
	public abstract int[] aiPut();
	
	//shuffle the rows and columns to change the order of traverse
	private void shuffle(int[] nums) {
		Random rd = new Random();
		for(int i = dimension-1; i > 0; i--) {
			int idx = rd.nextInt(i);
			int tmp = nums[idx];
			nums[idx] = nums[i];
			nums[i] = tmp;
		}
	}
	
	//pass the TicTacToe object to this ai player
	public void setTicTacToe(TicTacToe ttt) {
		this.ttt = ttt;
	}
	
	//check whether the place wins
	boolean isWon(int x, int y) {
		//either of them happens
		char mark = ttt.board[x][y];
		return rowForWin(x, mark) || colForWin(y, mark) || diagForWin(x, y, mark);
	}
	
	//check whether the xth row is composed of four currmarks.
	private boolean rowForWin(int x, char mark) {
		for(int j = 0; j < dimension; j++) {
			if(ttt.board[x][j] != mark) return false;
		}
		return true;
	}
	
	//check whether the yth column is composed of four currmarks.
	private boolean colForWin(int y, char mark) {
		for(int i = 0; i < dimension; i++) {
			if(ttt.board[i][y] != mark) return false;
		}
		return true;
	}
	//check whether the diagnose is composed of four currmarks.
	private boolean diagForWin(int x, int y, char mark) {
		//not on either diagnoses
		if(x != y && x + y != dimension-1) return false;
		if(x == y) {
			for(int i = 0; i < dimension; i++) {
				if(ttt.board[i][i] != mark) return false;
			}
		}else {
			for(int i = 0; i < dimension; i++) {
				if(ttt.board[i][dimension-1-i] != mark) return false;
			}
		}
		return true;
	}
		
}
