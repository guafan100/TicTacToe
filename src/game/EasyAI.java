package game;

public class EasyAI extends AIPlayer{
	
	public int[] aiPut(){
		this.currcount = ttt.count;
		this.board = ttt.board;
		int[] move = minValue();
		System.out.println("AI's move is ("+move[0]+", "+move[1]+")");
		return move;
	}
	
	// similar to alpha-beta algorithm in HardAI
	// but we get the worst move here
	// when it goes too deep, this ai treats it as a draw.
	private int[] minValue() {
		//generate all possible moves
		int max = Integer.MIN_VALUE, moveX = 0, moveY = 0;		
		for(int ii = 0; ii < dimension; ii++) {
			for(int jj = 0; jj < dimension; jj++) {
				// not occupied before
				int i = rows[ii], j = cols[jj];
				if(board[i][j] == ' ') {					
					board[i][j] = aiMark;
					int tmp = maxhelper(1, -1000, 1000, i, j);
					board[i][j] = ' ';
					if(tmp > max) {
						max = tmp;
						moveX = i;
						moveY = j;
					}
				}
			}
		}
		return new int[] {moveX, moveY};
	}
	
	private int maxhelper(int depth, int alpha, int beta, int x, int y) {
		//terminal state
		//player wins value = -1000
		if(isWon(x, y)) {
			return -1000;
		}
		//if draws or too deep
		if(depth + currcount == dimension * dimension || depth == level){
			return 0;
		}
		int v = Integer.MIN_VALUE;
		for(int ii = 0; ii < dimension; ii++) {
			for(int jj = 0; jj < dimension; jj++) {
				int i = rows[ii], j = cols[jj];
				if(board[i][j] == ' ') {
					board[i][j] = aiMark;
					v = Math.max(v, minhelper(depth+1, alpha, beta, i, j));
					board[i][j] = ' ';
					if(v >= beta) {
						return v;
					}
					alpha = Math.max(alpha, v);
				}
			}
		}

		return v;
	}
	//mark must be playerMark
	private int minhelper(int depth, int alpha, int beta, int x, int y) {
		//terminal state
		//ai wins value = 1000
		if(isWon(x, y)) {
			return 1000;
		}
		//if draws or too deep
		if(depth + currcount == dimension * dimension || depth == level){
			return 0;
		}		
		int v = Integer.MAX_VALUE;
		for(int ii = 0; ii < dimension; ii++) {
			for(int jj = 0; jj < dimension; jj++) {
				int i = rows[ii], j = cols[jj];
				if(board[i][j] == ' ') {
					board[i][j] = playerMark;
					v = Math.min(v, maxhelper(depth+1, alpha, beta, i, j));
					board[i][j] = ' ';
					if(v <= alpha) {
						return v;
					}
					beta = Math.min(v, beta);
				}
			}
		}
		return v;
	}
}
