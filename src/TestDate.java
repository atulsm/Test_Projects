import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TestDate {
	private static final SimpleDateFormat agentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		
		
		
		cal.setTimeInMillis(System.currentTimeMillis() + 5*60*60*1000);
		
		
		cal.getTime();
		System.out.println(System.currentTimeMillis());
		System.out.println(System.nanoTime());
		System.out.println("1381883504425581000");
		System.out.println(new Date(1381883504425581000L));
		System.out.println(new Date((1381883504425581000L)/1000000));
				
		System.out.println(agentDateFormat.format(cal.getTime()));
	}

}
