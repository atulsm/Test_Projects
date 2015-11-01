package bloomfilters;
import java.util.Random;


public class RandomStringGenerator {
	private static Random rand = new Random();
	private static char[] chars = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','g','h','j','k','l','z','x','c','v','b','b','n','m'};
	
	public static String generate(int size){
		char[] ret = new char[size];
		for(int i=0;i<size;i++){
			int loc = rand.nextInt(size);
			ret[i] = chars[loc];
		}		
		return new String(ret);
	}
	
	public static void main(String[] args) {
		for(int i=0;i< 10000;i++){
			System.out.println(generate(10));
		}
	}

}
