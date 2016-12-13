import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class TestTimeZone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss"); //$NON-NLS-1$
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		System.out.println(dateFormat.format(cal.getTime()));

	}
	
}
