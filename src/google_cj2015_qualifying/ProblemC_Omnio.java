package google_cj2015_qualifying;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 
 his contest is open for practice. You can try every problem as many times as you like, though we won't keep track of which problems you solve. Read the Quick-Start Guide to get started.
Small input
8 points	
Large input
26 points	
Problem

An N-omino is a two-dimensional shape formed by joining N unit cells fully along their edges in some way. More formally, a 1-omino is a 1x1 unit square, and an N-omino is an (N-1)omino with one or more of its edges joined to an adjacent 1x1 unit square. For the purpose of this problem, we consider two N-ominoes to be the same if one can be transformed into the other via reflection and/or rotation. For example, these are the five possible 4-ominoes:



And here are some of the 108 possible 7-ominoes:



Richard and Gabriel are going to play a game with the following rules, for some predetermined values of X, R, and C:

1. Richard will choose any one of the possible X-ominoes.
2. Gabriel must use at least one copy of that X-omino, along with arbitrarily many copies of any X-ominoes (which can include the one Richard chose), to completely fill in an R-by-C grid, with no overlaps and no spillover. That is, every cell must be covered by exactly one of the X cells making up an X-omino, and no X-omino can extend outside the grid. Gabriel is allowed to rotate or reflect as many of the X-ominoes as he wants, including the one Richard chose. If Gabriel can completely fill in the grid, he wins; otherwise, Richard wins.

Given particular values X, R, and C, can Richard choose an X-omino that will ensure that he wins, or is Gabriel guaranteed to win no matter what Richard chooses?
Input

The first line of the input gives the number of test cases, T. T lines follow. Each contains three space-separated integers: X, R, and C.
Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is either RICHARD (if there is at least one choice that ensures victory for Richard) or GABRIEL (if Gabriel will win no matter what Richard chooses).
Limits
Small dataset

T = 64.
1 ≤ X, R, C ≤ 4.
Large dataset

1 ≤ T ≤ 100.
1 ≤ X, R, C ≤ 20.
Sample

Input
  	
Output
 

4
2 2 2
2 1 3
4 4 1
3 2 3

	

Case #1: GABRIEL
Case #2: RICHARD
Case #3: RICHARD
Case #4: GABRIEL

In case #1, Richard only has one 2-omino available to choose -- the 1x2 block formed by joining two unit cells together. No matter how Gabriel places this block in the 2x2 grid, he will leave a hole that can be exactly filled with another 1x2 block. So Gabriel wins.

In case #2, Richard has to choose the 1x2 block, but no matter where Gabriel puts it, he will be left with a single 1x1 hole that he cannot fill using only 2-ominoes. So Richard wins.

In case #3, one winning strategy for Richard is to choose the 2x2 square 4-omino. There is no way for Gabriel to fit that square into the 4x1 grid such that it is completely contained within the grid, so Richard wins.

In case #4, Richard can either pick the straight 3-omino or the L-shaped 3-omino. In either case, Gabriel can fit it into the grid and then use another copy of the same 3-omino to fill in the remaining hole.
 
 * 
 * @author user
 *
 */
public class ProblemC_Omnio {
	
	private static int omSize = 3;
	private static int gridX = 4;
	private static int gridY = 4;
	
	private static List<Om> oms = new ArrayList<>();
	private static Grid grid;
	private static Stack<Om> stack = new Stack<>();


	public static void main(String[] args) {
		populate(omSize);
		//test();
		initStack();
		
					
		while(true){
			
		
			for(int i=0;i<gridY;i++){
				for(int j=0;j<gridX;j++){
					for(Om om : oms){
						if(grid.canAdd(om, new Pos(i,j))){
							grid.add(om, new Pos(i,j));
							stack.push(oms.get(i));
							break;
						}
					}
					
					Om om = stack.pop();
					//find next element to push
				}
			}
			
			if(stack.size() == 0){
				throw new RuntimeException("Stack empty");
			}
		}
		
		

		
		
		
	}
	
	private static void initStack(){
		for(int i=0;i<oms.size();i++){
			if(grid.canAdd(oms.get(i), new Pos(0,0))){
				grid.add(oms.get(i), new Pos(0,0));
				stack.push(oms.get(i));
				break;
			}
		}
	}

	private static void test() {
		for(int i=0;i<oms.size()-1;i++){
			oms.get(i).next = oms.get(i+1);
			System.out.println(i);
			oms.get(i).print();
		}
		oms.get(oms.size()-1).print();
		
		grid = new Grid(3, 2);
		grid.add(oms.get(4), new Pos(0,0));
		grid.add(oms.get(2), new Pos(1,0));
		grid.print();
		System.out.println(grid.isFull());

		grid.remove(oms.get(4), new Pos(0,0));
		grid.remove(oms.get(2), new Pos(1,0));
		grid.print();
		System.out.println(grid.isFull());
	}
	
	private static void populate(int size){
		switch(size){
			case 1:
				oms.add(new Om(new Pos[]{new Pos(0,0)}));
				break;
			case 2:
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(0,1)}));
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(1,0)}));
				break;
			case 3:
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(0,1),new Pos(0,2)}));
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(1,0),new Pos(2,0)}));
				
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(1,0),new Pos(1,1)}));
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(0,1),new Pos(-1,1)}));
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(0,1),new Pos(1,1)}));
				oms.add(new Om(new Pos[]{new Pos(0,0),new Pos(0,1),new Pos(1,0)}));
				break;
			case 4:
				oms.add(new Om(new Pos[]{new Pos(0,0)}));
				break;				
		}
	}
	
	private static class Grid{
		boolean mat[][];
		int x,y;
		
		Grid(int x,int y){
			mat = new boolean[y][x];
			this.x=x;
			this.y=y;
		}
		
		boolean isFull(){
			for(int i=0;i<y;i++){
				for(int j=0;j<x;j++){
					if(!mat[i][j]){
						return false;
					}
				}				
			}
			return true;
		}
		
		void add(Om om,Pos pos){
			for(Pos in : om.poss){
				if(mat[pos.y+in.y][pos.x+in.x]){
					System.out.println("Unexpected add");;
				}else{
					mat[pos.y+in.y][pos.x+in.x] = true;
				}
				
			}
		}
		
		void remove(Om om,Pos pos){
			for(Pos in : om.poss){
				if(!mat[pos.y+in.y][pos.x+in.x]){
					System.out.println("Unexpected remove");;
				}else{
					mat[pos.y+in.y][pos.x+in.x] = false;
				}
				
			}
		}
		
		boolean canAdd(Om om,Pos pos){
			for(Pos in : om.poss){
				if(pos.x+in.x > x){
					return false;
				}
				
				if(pos.y+in.y > y){
					return false;
				}
				
				if(mat[pos.y+in.y][pos.x+in.x]){
					return false;
				}
				
			}
			return true;
		}
		
		void print(){
			for(int i=0;i<y;i++){
				for(int j=0;j<x;j++){
					if(mat[i][j]){
						System.out.print("O\t");
					}else{
						System.out.print("\t");
					}
				}
				System.out.println("");
			}
		}
		
		
	}	
	
	
	private static class Om{
		Pos[] poss;
		boolean mat[][];
		Om next;
		
		Om(Pos[] poss){
			this.poss = poss;
			mat = new boolean[poss.length+1][poss.length+1];
			for(Pos pos:poss){
				mat[pos.y+1][pos.x+1] = true;
			}
		}
		
		void print(){
			for(int i=0;i<=poss.length;i++){
				for(int j=0;j<=poss.length;j++){
					if(mat[i][j]){
						System.out.print("O\t");
					}else{
						System.out.print("\t");
					}
				}
				System.out.println("");
			}
		}
		
		
	}
	
	private static class Pos{
		int x,y;
		Pos(int x, int y){
			this.x=x;
			this.y=y;
		}
	}

}
