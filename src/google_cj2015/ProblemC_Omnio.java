package google_cj2015;

import java.util.ArrayList;
import java.util.List;

public class ProblemC_Omnio {
	
	private static List<Om> oms = new ArrayList<>();
	private static Grid grid;

	public static void main(String[] args) {
		populate(3);
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
