package model;

import java.util.List;

import model.PuzzleGame.action;

public abstract class Player {
	

	/**
	 * Implement this method to solve a given PuzzleGame. This method should return a List of actions
	 *  that lead to the solution state.
	 * @return List of actions to solve the board.
	 * @param game The puzzle game to solve
	 * 
	 */
	
	public abstract List<action> solve(PuzzleGame game);

	
	/**
	 * Finds a solution and applies it to the board. Only used by the GUI. 
	 * @param game The board game.
	 */
	public void solveAndApply(PuzzleGame game) {
		List<action> solution = solve(game);
		for (int i=0;i< solution.size(); ++i) {
			game.performAction(solution.get(i));
		}
	}
	
	
}
