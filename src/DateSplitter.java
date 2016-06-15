

import java.util.Date;

import org.joda.time.DateTime;

/**
 * Simple java class to split two date ranges
 * 
 * @author satul
 *
 */
public final class DateSplitter {
	
	private final DateTime from;
	private final DateTime to;
	private final int interval;
	
	private volatile DateTime current;
	
	public DateSplitter(Date from, Date to, int interval){
		this.from = new DateTime(from);
		this.to = new DateTime(to);
		this.interval = interval;
		
		current = this.from;
	}
	
	public synchronized Date next(){
		if(current.isAfter(to.getMillis())){
			return null;
		}
		
		current = current.plusSeconds(interval);
		
		//This will be the last date
		if(current.isAfter(to.getMillis())){
			return to.toDate();
		}
		
		return current.toDate();
	}	
	
	public static void main(String[] args) {
		DateSplitter test = new DateSplitter(DateTime.now().minusDays(1).toDate(),DateTime.now().toDate(), 60);		
		Date next;
		while( (next=test.next()) != null){
			System.out.println(next);
		}
	}

}
