/**
 * @ project 1
 * @author Zhong, Xiayue  Matrikelnummer: 10017312
 * @author Xiao,Qing Matrikelnummer: 10014851
 */

package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import model.PuzzleGame.action;

public class RandomPlayer extends Player{
	
	public class Node {
		private Integer[][] _element;
		
		public Integer[][] getElement(){
			return _element;
		}
		public void setElement(Integer[][] element) {
			this._element = element;
		}
		public  Node() {			
			_element = new Integer[3][3];
		}
		public  Node(Integer[][] i) {			
			_element = i;
		}
		
		@Override
	    public int hashCode() {
			
			return (_element[0][0]<<8+_element[0][1]);	
			//return _element[0][0];
		}
		@Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (!(obj instanceof Node))
	            return false;
	        Node other = (Node) obj;
	        if(this.hashCode()!=other.hashCode())
	        	return false;
	        for(int i=0;i<3;i++)
				for(int j=0;j<3;j++) {
					if(other._element[i][j] != this._element[i][j])
						return false;
				}
	        return true;
	    }	
	};
	
	public RandomPlayer() {
		// TODO Auto-generated constructor stub
	}
	public List<action> solve(PuzzleGame game){
		//List<action> result = new ArrayList<>();
		Integer[][] board = game.getGameBoard();
		List<action> result = new ArrayList<>();
		
		result = bfsAlgorithm(game,board);
		//System.out.println(board[0][0]);
		
/*
		Random r = new Random();
		for(int i=0;i<5;i++) {
			action[] actions = game.getPossibleActions(board);
			int nextActionIndex = r.nextInt(actions.length);
			action nextAction = actions[nextActionIndex];
			
			result.add(nextAction);
			board = game.computeAction(nextAction, board);
		}
		*/
		return result;
	}
	
	
	/**
	 * bfs algorithm
	 * @param start
	 */
	public List<action> bfsAlgorithm(PuzzleGame game,Integer[][] start) {
		
		Node goal = new Node();
		Node root = new Node();
		
		Integer[][] g = new Integer[3][3];
		for(int row=0;row<3;row++)
			for(int column=0;column<3;column++) {
				g[row][column] = (row*3)+column+1;				
			}
		g[2][2] = 0;	
		/*
		for(int row=0;row<3;row++)
			for(int column=0;column<3;column++) {
				start[row][column] =9 - ((row*3)+column+1);				
			}
		start[2][2] = 0;	
		*/
		root.setElement(start);
		goal.setElement(g);
		
		
		
		
		//Queue<Integer[][]> search_queue = new LinkedBlockingQueue<>();	
		Queue<Node> search_queue = new LinkedBlockingQueue<>();			
		Set<Node> _is_visited = new HashSet<>();
		
		List<action> path = new ArrayList<>();
		
		search_queue.add(root);
		_is_visited.add(root);
		_is_visited.hashCode();
		int count = 0;
		Node current = new Node();
		do {
			count++;
			//printQueue(search_queue,count);
			//printHashSet(_is_visited,count);
			current = search_queue.remove();
						
			action[] act = game.getPossibleActions(current.getElement());

			for(action a: act) {
				Integer[][] temp = game.computeAction(a, current.getElement());
				Node node = new Node(temp);
				if(!_is_visited.contains(node)) {
				//if(!containElement(_is_visited,node)) {
					search_queue.add(node);
					//path.add(a);
					_is_visited.add(node);					
				}
			}					
			print(current.getElement(),count);			

		}while(!game.isSolution(current.getElement()));	
		System.out.println("i got the node!"+count);
		return path;
	}
	public void print(Integer[][] board,int count) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++)
				System.out.print(board[i][j]);
			System.out.println(" ");
		}
		System.out.println("-----"+count);
	}
	public void printQueue(Queue<Node> search_queue,int count) {
		System.out.println("-----Queue-----开始：");
		for(Node node:search_queue) {
			Integer[][] board = node.getElement();
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++)
					System.out.print(board[i][j]);				
			}
			System.out.println(" ");
		}
		
		System.out.println("-----Queue-----结束："+count);
	}
	public void printHashSet(Set<Node> _is_visited,int count) {
		System.out.println("-----Hashset-----开始：");
		for(Node node:_is_visited) {
			Integer[][] board = node.getElement();
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++)
					System.out.print(board[i][j]);				
			}
			System.out.println(" ");
		}
		
		System.out.println("-----Hashset-----结束："+count);
	}
	
	public boolean containElement(Set<Node> set, Node node) {		
		for(Node n: set) {
			if(n.equals(node)) return true;
		}		
		return false;
	}

}
