import java.util.Date;


public class TestPerformance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("Creating new Date " + new Date());
		long end = System.currentTimeMillis();
		System.out.println("Time taken to create date is: " + (end-start) + " milliseconds");

	}

}
