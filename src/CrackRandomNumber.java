import java.util.Random;


public class CrackRandomNumber {

    private static long multiplier = 0x5DEECE66DL;
    private static long addend = 0xBL;
    private static long mask = (1L << 48) - 1;
    
	public static void main(String[] args) {
	    Random random = new Random();  
	    long v1 = random.nextInt();  
	    long v2 = random.nextInt();  
	    for (int i = 0; i < 65536; i++) {  
	        long seed = v1 * 65536 + i;  
	        if ((((seed * multiplier + addend) & mask) >>> 16) == v2) {  
	            System.out.println("Seed found: " + seed);  
	            break;  
	        }  
	    }  
	}
	

}
