
public class TestLogarithm {

	public static void main(String[] args) {
		System.out.println(calculateNumSlice(50000,50000));	
		System.out.println(calculateNumSlice(50000,100000));	
		System.out.println(calculateNumSlice(50000,200000));	
		System.out.println(calculateNumSlice(50000,300000));	
		System.out.println(calculateNumSlice(50000,400000));	


		System.out.println(calculateNumSlice(50000,100000000)); //20
		System.out.println(calculateNumSlice(50000,200000)); //4
		System.out.println(calculateNumSlice(50000,2000000)); //10
		System.out.println(calculateNumSlice(2,100000000));
		System.out.println(calculateNumSlice(1,100000000));		//20
		System.out.println(calculateNumSlice(100,200000));		
		System.out.println(calculateNumSlice(1000,150000));		

	}
		
	private static int calculateNumSlice(long target, long original){		
		long varience = original/target;
		long logVarience = (long)(Math.log(varience)/Math.log(2));
		
		long slice = 2* logVarience;
		if(slice == 0){
			slice = 2;
		}
		
		if(slice > 20){
			slice =20;
		}
				
		return (int)slice;
	}

}