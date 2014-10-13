import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;


public class TestTimeZoneLocalize {

	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		String[] ids=TimeZone.getAvailableIDs();
		Locale loc = new Locale("fr","FR");
		for (int i=0; i<ids.length; i++){
		System.out.println(TimeZone.getTimeZone(ids[i]).getDisplayName(loc));
		} 
	}
	
	public static void main(String[] args) {
		String[] ids=TimeZone.getAvailableIDs();

		for (int i=0; i<ids.length; i++){
			TimeZone tz =  TimeZone.getTimeZone(ids[i]);
			System.out.println(tz.getID() + "\t" + tz.getDisplayName());
		} 

	}
	
	public static void main2(String[] args) {
		String[] ids=TimeZone.getAvailableIDs();
		String[] displayNames = new String[ids.length+1];

		for (int i=0; i<ids.length; i++){
			displayNames[i] = TimeZone.getTimeZone(ids[i]).getDisplayName();
			//System.out.println(displayNames[i]);
		} 
		
		System.out.println("Total timexones based on ID's:" + ids.length);
		
		Set<String> uniqueIds = new HashSet<String>(Arrays.asList(displayNames));
		System.out.println("Total unique timezones:" + uniqueIds.size());
		
		System.out.println(uniqueIds);
	}

}
