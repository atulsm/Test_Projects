package google_cj2015;

public class ProblemC_Omnio {

	public static void main(String[] args) {
		Om om = new Om(new Pos[]{new Pos(0,0),new Pos(0,1),new Pos(0,2),new Pos(0,3)});
		om.print();
		om = new Om(new Pos[]{new Pos(0,0),new Pos(1,0),new Pos(2,0),new Pos(3,0)});
		om.print();
	}
	
	private static class Om{
		Pos[] poss;
		boolean mat[][];
		
		Om(Pos[] poss){
			this.poss = poss;
			mat = new boolean[poss.length+1][poss.length+1];
			for(Pos pos:poss){
				mat[pos.x+1][pos.y+1] = true;
			}
		}
		
		void print(){
			for(int i=0;i<=poss.length;i++){
				for(int j=0;j<=poss.length;j++){
					if(mat[j][i]){
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
