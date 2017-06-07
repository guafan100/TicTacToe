package game;

public class TicTacToe {
	//the play board
    char[][] board;
    
	//the mark the play will use
	char currmark;
	
	//the size of the play board
	static final int dimension = 4;
	
	//mark of player
	static final char playerMark = 'O';
	
	//mark of ai
	static final char aiMark = 'X';
	
	//difficulty can be 1,2,3, which represents easy,intermediate and difficult
	int difficulty;
	
	//how many symbols have been placed
	int count;
	
	//gameResult: -1: continue, 0: player draws, 1: ai draws, 
	//2: player wins, 3: ai wins
	int gameResult;
	
	//save the move for UI
	int[] nextMove;
	
	//the AI player to play with human
	AIPlayer aiplayer;
	
	public TicTacToe() {
		//the default difficulty is 3
		this(3);
	}
	
	public TicTacToe(int diff) {
		//check if it is illegal
		if(diff <= 0 || diff > 3) 
			throw new IllegalArgumentException("the input should be 1,2 or 3");
		board = new char[dimension][dimension];
		currmark = playerMark;
		count = 0;
		gameResult = -1;
		nextMove = new int[2];
		//set up the AIPlayer according to the difficulty
		aiplayer = diff == 1? new EasyAI(): 
				diff == 2? new MedAI(): new HardAI();
		aiplayer.setTicTacToe(this);
		//initialize the play board
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				board[i][j] = ' ';
			}
		}
		difficulty = diff;
	}
	
	//put the mark in the given location.
	//check whether it wins or draws.
	//if no win or draw, AI will place its symbol.
	//check whether it wins or draws.
	public void put(int x, int y) {
		//out of range
		if(x < 0 || y < 0 || x >= dimension || y >= dimension) {
			System.out.println("Invalid input!");
			return;
		}
		else {
			if(board[x][y] != ' ') {
				System.out.println("This position has been occupied");
				return;
			}else {
				board[x][y] = currmark;
				//count plus one once a new symbol is placed
				count++;
			}
		}
		System.out.println("after user's place");
		printboard();
		//check the result for the player's move
		if(isWon(x, y)) {
			gameResult = 2;
			System.out.println(currmark+" WINNER!");
		}else if(isFull()) {
			gameResult = 0;
			System.out.println("DRAW!");
		}else {
			aiMove();
		}		
	}
	
	public void aiMove() {
		//if not draw or win, it is turn for AI
		//run aiPut() to determine the move of AI.
		int[] move = aiplayer.aiPut();
		
		board[move[0]][move[1]] = currmark;
		nextMove[0] = move[0]; nextMove[1] = move[1];
		//count plus one once a new symbol is placed
		count++;
		System.out.println("after AI's place");
		printboard();
		//after place, we check whether it wins or draws
		if(isWon(move[0], move[1])) {
			gameResult = 3;
			System.out.println(currmark+" WINNER!");
		}else if(isFull()) {
			gameResult = 1;
			System.out.println("DRAW!");
		}
	}
	
	//when all the grids have been occupied, it is full and it draws
	private boolean isFull() {
		if(count == dimension * dimension) {
			return true;
		}
		//change the mark for next player if there is any empty grid
		currmark = currmark == aiMark? playerMark: aiMark;
		return false;
	}
	
	//check whether this place leads to a win
	private boolean isWon(int x, int y) {
		//either of them happens
		char mark = board[x][y];
		return rowForWin(x, mark) || colForWin(y, mark) || diagForWin(x, y, mark);
	}
	
	//check whether the xth row is composed of four currmarks.
	private boolean rowForWin(int x, char mark) {
		for(int j = 0; j < dimension; j++) {
			if(board[x][j] != mark) return false;
		}
		return true;
	}
	
	//check whether the yth column is composed of four currmarks.
	private boolean colForWin(int y, char mark) {
		for(int i = 0; i < dimension; i++) {
			if(board[i][y] != mark) return false;
		}
		return true;
	}
	//check whether the diagnose is composed of four currmarks.
	private boolean diagForWin(int x, int y, char mark) {
		//not on either diagnoses
		if(x != y && x + y != dimension-1) return false;
		if(x == y) {
			for(int i = 0; i < dimension; i++) {
				if(board[i][i] != mark) return false;
			}
		}else {
			for(int i = 0; i < dimension; i++) {
				if(board[i][dimension-1-i] != mark) return false;
			}
		}
		return true;
	}
	
	public void printboard() {
		for(int i = 0; i < dimension; i++) {			
			for(int j = 0; j < dimension; j++) {
				System.out.print("|"+board[i][j]);
			}
			System.out.println("|");			
		}
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println("1 2 3");
//		int d = new Scanner(System.in).nextInt();
//		TicTacToe ttt = new TicTacToe(d);
//
//
//		while(ttt.count < 16) {
//			System.out.println("Please input your move");
//			Scanner in = new Scanner(System.in);
//			int[] input = new int[2];
//			int i = 0;
//			while(in.hasNext()) {
//				if(in.hasNextInt()) {
//					input[i++] = in.nextInt();
//				}
//				if(i == 2) break;
//			}
//			ttt.put(input[0], input[1]);
//		}
//		//ttt.put(2, 1);
//		
//	}
//	public static int test(char[][] board) {
//		int cnt = 0;
//		for(char[] arr: board) {
//			for(char c : arr) {
//				if(c != ' ')
//					cnt++;
//			}
//		}
//		return cnt;
//	}

}
