import org.joda.time.DateTime;
import org.joda.time.Interval;

public class TestJodaTime {

	public static void main(String[] args) {
		DateTime now = DateTime.now();
		DateTime tomorrowStart = now.plusDays(1).withTimeAtStartOfDay().plusMinutes(30);
		long scheduleDelay = new Interval(now, tomorrowStart).toDurationMillis();
		System.out.println(tomorrowStart.toDate());
	}

}
