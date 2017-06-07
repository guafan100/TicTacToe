package game;

public class MedAI extends AIPlayer{
	
	public int[] aiPut() {
		this.currcount = ttt.count;
		this.board = ttt.board;
		int[] move = getNextMove();
		return move;
	}
	
	//in this difficulty, the AI can only consider one step away
	private int[] getNextMove() {
		//generate all possible moves
		//cnt is for the number of ways for player to win
		//make sure the cnt is the least when we have the max value
		int max = Integer.MIN_VALUE, moveX = 0, moveY = 0, cnt = Integer.MAX_VALUE;
		for(int ii = 0; ii < dimension; ii++) {
			for(int jj = 0; jj < dimension; jj++) {
				int i = rows[ii], j = rows[jj];
				if(board[i][j] == ' ') {
					board[i][j] = aiMark;
					int[] tmp = minhelper(i, j);
					board[i][j] = ' ';
					if(tmp[0] > max) {
						max = tmp[0];
						moveX = i;
						moveY = j;
					}else if(tmp[0] == max && tmp[1] < cnt) {
						cnt = tmp[1];
						moveX = i;
						moveY = j;
					}
				}
			}
		}
		return new int[]{moveX, moveY};
	}
	
	//depth == 2, terminal state
	private int maxhelper(int x, int y) {
		//player wins value = -1000, or return 0
		return isWon(x, y)? -1000: 0;
	}
	
	//depth == 1
	private int[] minhelper(int x, int y) {
		int v = Integer.MAX_VALUE, cnt = 0;
		if(isWon(x, y)) {
			v = 1000;
		}else if(currcount + 1 == dimension * dimension) {
			v = 0;
		}else {
			for(int ii = 0; ii < dimension; ii++) {
				for(int jj = 0; jj < dimension; jj++) {
					int i = rows[ii], j = cols[jj];
					if(board[i][j] == ' ') {
						board[i][j] = playerMark;
						int tmp = maxhelper(i, j);
						if(tmp == -1000) cnt++;
						v = Math.min(v, tmp);
						board[i][j] = ' ';
					}
				}
			}
		}
		return new int[]{v, cnt};
	}
}
