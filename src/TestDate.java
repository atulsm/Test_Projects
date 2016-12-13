import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TestDate {
	private static final SimpleDateFormat agentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, 2000);
		System.out.println(cal.getTime());

		
		System.out.println(TimeZone.getDefault().getRawOffset());
		System.out.println(330*60*1000);
		
		cal.setTimeInMillis(System.currentTimeMillis() + 5*60*60*1000);
		System.out.println(new Date(1459270800000L));
		
		cal.getTime();
		System.out.println(System.currentTimeMillis());
		System.out.println(System.nanoTime());
		System.out.println("1381883504425581000");
		System.out.println(new Date(1381883504425581000L));
		System.out.println(new Date((1381883504425581000L)/1000000));
				
		System.out.println(agentDateFormat.format(cal.getTime()));
		
		TimeZone defaultTZ = TimeZone.getDefault();
		
		TimeZone utc = TimeZone.getTimeZone("UTC");
		Calendar calUtc = Calendar.getInstance(utc);
		long diff = calUtc.getTime().getTime() - new Date().getTime();

		Thread.sleep(10000);

	}

}
