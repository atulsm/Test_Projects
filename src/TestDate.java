import java.util.Calendar;
import java.util.Date;


public class TestDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		
		
		
		cal.setTimeInMillis(System.currentTimeMillis());
		
		
		cal.getTime();
		System.out.println(System.currentTimeMillis());
		System.out.println(System.nanoTime());
		System.out.println("1381883504425581000");
		System.out.println(new Date(1381883504425581000L));
		System.out.println(new Date((1381883504425581000L)/1000000));
	}

}
