import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class TestTimer {

	public static void main(String[] args) {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("Running at " + new Date());
			}
		};
		
		System.out.println("Staring at " + new Date());
		timer.scheduleAtFixedRate(task, 5000, 10000);
		

	}

}
