package model;

import java.util.Observable;
import java.util.Vector;

public class PuzzleGame extends Observable {

	
	public enum action {
		UP, DOWN, LEFT, RIGHT;
	} 

	private Integer[][] gameBoard;
	private int zX;
	private int zY;
	private Vector<action> log;
	private boolean won;
	private boolean wasReset;
	
	/**
	 * Standard Constructor
	 */
	public PuzzleGame() {
		gameBoard = new Integer[3][3];
		log = new Vector<>();
		wasReset=false;
		
		reset();
	}
	
	/**
	 * Setter for the GUI.
	 */
	public void unsetReset() {
		wasReset=false;
	}
	
	/**
	 * Checks, if the current state of the game is won. Used by the GUI.
	 * @return True if won, false otherwise
	 */
	public boolean isWon() {
		return won;
	}
	
	/**
	 * Computes the possible actions for a given board
	 * @param board The board
	 * @return Array of possible actions
	 */
	
	public action[] getPossibleActions(Integer[][] board) {
		int zY=0;
		int zX=0;
		
		for (int i=0; i<board.length; ++i) {
			for(int j=0; j<board[i].length; ++j) {
				if (board[i][j] == 0) {
					zY=i;
					zX=j;
				}
			}
		}	
		
		Vector<action> result = new Vector<>();
			
		if (zY < 2) {
			result.add(action.DOWN);
		}
		if (zY > 0) {
			result.add(action.UP);
		}
		if (zX < 2) {
			result.add(action.RIGHT);
		}
		if (zX > 0) {
			result.add(action.LEFT);
		}		
		return result.toArray(new action[result.size()]);		
	}
	
	/**
	 * Performs an action on the current board of the game. This will result in 
	 * an actual change of the game state.
	 * @param a Action to perform
	 */
	
	public void performAction(action a) {
		gameBoard = computeAction(a, this.gameBoard);
		log.addElement(a);
		for (int i=0; i<gameBoard.length; ++i) {
			for(int j=0; j<gameBoard[i].length; ++j) {
				if (gameBoard[i][j] == 0) {
					zY=i;
					zX=j;
				}
			}
		}
	}
	
	/**
	 * Standard getter for GUI
	 * @return x-Coordinate of the zero-tile
	 */
	public int getZX() {
		return zX;
	}
	
	/**
	 * Standard getter for GUI
	 * @return y-Coordinate of the zero-tile
	 */
	public int getZY() {
		return zY;
	}
	
	/**
	 * Computes the impact of an action to a given board. This method
	 * will not change the state of the actual puzzle. Use this method
	 * to explore new paths.
	 * @param a The action to perform.
	 * @param board The board to perfom the action on-
	 * @return The changed board.
	 */
	public Integer[][] computeAction(action a, Integer[][] board) {
		Integer[][] result = Utility.deepCopyIntegerArray(board);	
		int zY=0;
		int zX=0;
		for (int i=0; i<result.length; ++i) {
			for(int j=0; j<result[i].length; ++j) {
				if (result[i][j] == 0) {
					zY=i;
					zX=j;
				}
			}
		}
			
		switch (a) {
		case UP: {
			int temp = result[zY-1][zX];
			result[zY-1][zX]=0;
			result[zY][zX]=temp;
			zY -=1;
			break;
		}
		case DOWN: {
			int temp = result[zY+1][zX];
			result[zY+1][zX]=0;
			result[zY][zX]=temp;
			zY +=1;
			break;	
		}
		case LEFT: {
			int temp = result[zY][zX-1];
			result[zY][zX-1]=0;
			result[zY][zX]=temp;
			zX -=1;
			break;	
		}
		case RIGHT: {
			int temp = result[zY][zX+1];
			result[zY][zX+1]=0;
			result[zY][zX]=temp;
			zX +=1;
			break;	
		}
		}
		return result;
		
	}
	
	/**
	 * Standard getter for the current board.
	 * @return The current board.
	 */
	public Integer[][] getGameBoard() {
		return gameBoard;
	}
	
	/**
	 * Standard getter for GUI
	 * @return The log of performed actions.
	 */
	
	public Vector<action> getLog() {
		return log;
	}
	
	/**
	 * Creates a solution state. Used in the constructor.
	 * 
	 */
	private void makeSolutionState() {
		for (int i=0; i<gameBoard.length; ++i) {
			for (int j=0; j<gameBoard[i].length; ++j) {
				gameBoard[i][j]=i*3+j+1;
			}
		}
		gameBoard[2][2]=0;
		zX=2;
		zY=2;
	}
	/** Resets the board to an (unsolved) state. Used by the GUI.
	 *
	 */
	public void reset() {
		makeSolutionState();
		while (isSolution(this.gameBoard)) {
			randomizeBoard();
		}
		log.clear();
		won =false;
		wasReset=true;
		
		setChanged();
		notifyObservers();		
	}
	
	/**
	 * Returns an heuristic distance to the solution state for the given
	 * board. The heuristic is the Manhatten distance.
	 * @param board The board thats heuristics should be calculated.
	 * @return The heuristic distance.
	 */
	
	public int getHeuristicValue(Integer[][] board) {
		int result =0;
		for (int i=0; i<board.length; ++i) {
			for (int j=0; j<board[i].length; ++j) {
				int targetCol;
				int targetRow;
				if (board[i][j] == 0) {
					targetCol=2;
					targetRow=2;
				} else {
					targetCol = (board[i][j]-1) % 3;
					targetRow = (int)Math.floor((board[i][j]-1)/ 3.0);
				}
				result += Math.abs(targetCol-j) + Math.abs(targetRow-i);
			}
		}
		return result;
	}
	
	
	/**
	 * Checks if a given board is in a solution state.
	 * @param board The board
	 * @return True if solved, false otherwise
	 */
	public boolean isSolution(Integer[][] board) {
		boolean result=true;
		for (int i=0; i<board.length; ++i) {
			for (int j=0; j<board[i].length; ++j) {
				if (((i ==2 && j ==2) && board[i][j]!=0) || ((i!=2 || j!=2) && board[i][j] != (i*3+j+1))) {
					result=false;
				}
			}
		}
		return result;
	}
	
	/**
	 * Checks if this board is solved. Used by the GUI.
	 */
	public void checkFinished() {
		won = isSolution(gameBoard);

		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * Used to create a random (solvable) puzzle.
	 */
	private void randomizeBoard() {
		for (int i=0; i<100; ++i) {
			action[] actions = getPossibleActions(gameBoard);
			int next =(int)Math.ceil(Math.random()* actions.length)-1;
			performAction(actions[next]);
		}	

	}
	
	/**
	 * Computes a string representation of the given board.
	 * @param board The board to be presented as a string.
	 * @return The string representation of the board.
	 */
	public String boardToString(Integer[][] board) {
		String result="";
		for (int i=0; i<board.length; ++i) {
			String l="";
			for (int j=0; j<board[i].length; ++j) {
				l+=" "+board[i][j];
			}
			result += l.substring(1)+"\n";
		}
		return result;
	}
	
	
	/**
	 * Checks if the last action was a reset. Only used for GUI
	 * @return Reset status
	 */
	public boolean wasReset() {
		return wasReset;
	}


	@Override
	public String toString() {
		return boardToString(gameBoard);
	}
}
