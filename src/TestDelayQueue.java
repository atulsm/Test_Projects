import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class TestDelayQueue {

	public static class DelayedString implements Delayed {
	    private String data;
	    public long delaySeconds;
	    
	    public DelayedString(String data, long delaySeconds) {
	    	this.data = data;
	    	this.delaySeconds=delaySeconds;
		}
	    
	    public String getData() {
	    	return data;
	    }
	    
		@Override
		public int compareTo(Delayed o) {
			return (int) ((int) delaySeconds - ((DelayedString)o).delaySeconds);
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(delaySeconds, TimeUnit.MILLISECONDS);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		DelayQueue<DelayedString> dqueue = new DelayQueue<DelayedString>();
		dqueue.put(new DelayedString("Atul",10));
		dqueue.put(new DelayedString("Soman",5));
		dqueue.put(new DelayedString("Alen",20));
		
		DelayedString data = null;
		while((data=dqueue.take()) != null) {
			System.out.println(data.getData());
		}
		
		
	}

}
