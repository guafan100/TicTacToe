package game;

public class HardAI extends AIPlayer{
	//start time
	private long startTime;
	
	//whether cut off occurs
	private boolean cutoff;
	
	//max depth reached
	private int maxdepth;
	
	//number of nodes generated
	private int numGenerated;
	
	//number of times pruning occurred within max-value
	private int maxcnt;
	
	//number of times pruning occurred within min-value
	private int mincnt;
	
	//time interval
	private static final long interval = 5000000000l;
	
	public int[] aiPut() {
		//every time it's turn for ai to place, we pass the value of game's board and count
		this.currcount = ttt.count;
		this.board = ttt.board;
		
		//run alpha-beta algorithm to determine the best move 
		int[] move = maxValue();
		
		//print the information
		double cost = (System.nanoTime() - startTime)/1000000000.0;
		System.out.println("AI's move is ("+move[1]+", "+move[2]+")");
		System.out.println("FOR THIS MOVE:");
		System.out.println("it takes "+cost+" s");
		String str = cutoff? "cut off occurs": "cut off not occurs";
		System.out.println(str);
		System.out.println("maximum depth reached: "+ maxdepth);
		System.out.println("nodes generated: "+ numGenerated);
		System.out.println("max pruning: "+maxcnt+" min pruning: "+mincnt);
		
		return new int[] {move[1], move[2]};
	}
	
	//run alpha-beta algorithm in HardAI
	//when time out, cut off using evaluation function
	private int[] maxValue() {
		//initialize some counters
		cutoff = false;
		maxdepth = 0;
		numGenerated = 1;
		maxcnt = 0;
		mincnt = 0;
		startTime = System.nanoTime();
		
		//generate all possible moves using the shuffled index
		int max = Integer.MIN_VALUE, moveX = 0, moveY = 0;		
		for(int ii = 0; ii < dimension; ii++) {
			for(int jj = 0; jj < dimension; jj++) {
				// not occupied before
				int i = rows[ii], j = cols[jj];
				if(board[i][j] == ' ') {					
					board[i][j] = aiMark;
					//it is of depth 0 in this method
					//alpha is -1000, beta is 1000
					int tmp = minhelper(1, -1000, 1000, i, j);
					board[i][j] = ' ';
					if(tmp > max) {
						max = tmp;
						moveX = i;
						moveY = j;
					}
				}
			}
		}
		return new int[] {max, moveX, moveY};
	}
	
	//argument x and y are efficient to check wins
	//mark must be aiMark when running maxhelper
	private int maxhelper(int depth, int alpha, int beta, int x, int y) {
		numGenerated++;
		
		//terminal state
		//player wins value = -1000
		if(isWon(x, y)) {
			maxdepth = Math.max(maxdepth, depth);
			return -1000;
		}
		//if draws
		if(depth + currcount == dimension * dimension){
			maxdepth = depth;
			return 0;
		}
		
		//check whether cut off is needed
		if(!cutoff && System.nanoTime() - startTime >= interval) {
			maxdepth = Math.max(maxdepth, depth);
			cutoff = true;
		}
		if(cutoff) {	
			if(depth > level) {				
				return Integer.MAX_VALUE;
			}
			//depth == level is like a terminal state
			else if(depth == level) {
				return eval();
			}
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
						maxcnt++;
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
		numGenerated++;

		//terminal state
		//ai wins value = 1000
		if(isWon(x, y)) {
			maxdepth = Math.max(maxdepth, depth);
			return 1000;
		}
		//if draws
		if(depth + currcount == dimension * dimension){
			maxdepth = depth;
			return 0;
		}
		
		//check whether cut off is needed
		if(!cutoff && System.nanoTime() - startTime >= interval) {
			maxdepth = Math.max(maxdepth, depth);
			cutoff = true;
		}
		if(cutoff) {			
			if(depth > level) {
				return Integer.MIN_VALUE;
			}
			//depth == level is like a terminal state
			else if(depth == level) {
				return eval();
			}
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
						mincnt++;
						return v;
					}
					beta = Math.min(v, beta);
				}
			}
		}
		return v;
	}
	
	//Eval(s)=6X3 +3X2(s)+X1(s)−(6O3 +3O2(s)+O1(s))
	private int eval() {
		//index indicates the number: numOs[2] is O2(s)...
		int[] numOs = new int[5], numXs = new int[5];		
		checkColsAndRows(numOs, numXs);		
		checkDiag(numOs, numXs);		
		return 6*numXs[3] + 3*numXs[2] + numXs[1]
				- (6*numOs[3] + 3*numOs[2] + numOs[1]);
		
//		int result = 0;
//		for(int i = 0; i < dimension; i++) {
//			for(int j = 0; j < dimension; j++) {
//				if(board[i][j] == ' ') continue;
//				if(i == j || i + j == dimension -1) {
//					if(board[i][j] == aiMark) result += 2;
//					else result -= 2;
//				}else {
//					if(board[i][j] == aiMark) result ++;
//					else result --;
//				}
//			}
//		}
//		return result;
	}
	
	//check the number of diags with exactly n Xs without Os or Os without Xs
	private void checkDiag(int[] numOs, int[] numXs) {
		//count1 is for left diag, count2 is for right diag
		int count1 = -1, count2 = -1;
		char curr1 = ' ', curr2 = ' ';
		
		for(int i = 0; i < dimension; i++) {
			//left diag
			if(board[i][i] != ' ') {
				if(curr1 == ' ') {
					curr1 = board[i][i];
					count1 = 1;
				}else if(curr1 != board[i][i]) {
					count1 = -1;
					break;
				}else {
					count1++;
				}
			}
		}
		for(int i = 0; i < dimension; i++) {
			//right diag
			int j = dimension - i - 1;
			if(board[i][j] != ' ') {
				if(curr2 == ' ') {
					curr2 = board[i][j];
					count2 = 1;
				}else if(curr2 != board[i][j]) {
					count2 = -1;
					break;
				}else {
					count2++;
				}
			}			
		}
		addNum(numOs, numXs, count1, curr1);
		addNum(numOs, numXs, count2, curr2);
	}
	
	//count the number of Os and Xs
	private void addNum(int[] numOs, int[] numXs, int count, char curr) {
		if(count != -1) {
			if(curr == aiMark) numXs[count]++;
			else numOs[count]++;
		}
	}
	
	//check the number of rows and cols with exactly n Xs without Os or Os without Xs
	private void checkColsAndRows(int[] numOs, int[] numXs) {
		for(int i = 0; i < dimension; i++) {
			int count1 = -1, count2 = -1;
			char curr1 = ' ', curr2 = ' ';
			for(int j = 0; j < dimension; j++) {
				//check cols
				if(board[j][i] != ' ') {
					if(curr1 == ' ') {
						curr1 = board[j][i];
						count1 = 1;
					}else if(board[j][i] != curr1) {
						count1 = -1;
						break;
					}else {
						count1++;
					}
				}
			}
			for(int j = 0; j < dimension; j++) {
				//check rows
				if(board[i][j] != ' ') {
					if(curr2 == ' ') {
						curr2 = board[i][j];
						count2 = 1;
					}else if(board[i][j] != curr2) {
						count2 = -1;
						break;
					}else {
						count2++;
					}
				}
			}
			addNum(numOs, numXs, count1, curr1);
			addNum(numOs, numXs, count2, curr2);	
		}
	}
}
