import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TestExecutorServicePerformance {

	//private static ExecutorService exec = Executors.newCachedThreadPool();  //90000
	//private static ExecutorService exec = Executors.newSingleThreadExecutor(); //125000
	//private static ExecutorService exec = Executors.newFixedThreadPool(10); //125000
	private static ExecutorService exec = Executors.newWorkStealingPool(); //111111
	//private static ExecutorService exec = Executors.newWorkStealingPool(10); //111111

	
	private static int count = 0;
	private static EventWorker worker = new EventWorker();
	private static final int size = 1000000;

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		ArrayList<Future<String>> futures = new ArrayList<Future<String>>(size);
		
		while(true){
			worker.setEvent("event" + count++);
			Future<String> future = exec.submit(worker);
			futures.add(future);
			//System.out.println(future.get(50, TimeUnit.SECONDS));		
			
			if(count==size) {
				break;
			}
		}
		
		for(Future<String>future : futures) {
			System.out.println(future.get());
		}
		
		System.out.println("EPS: " + size/ ((System.currentTimeMillis() - start)/1000));
	}
	
	private static class EventWorker implements Callable<String>{
		private String event;
		
		public void setEvent(String event){
			this.event = event;
		}	
		
		public String process() {
			return event;
		}

		@Override
		public String call() throws Exception {
			return process();
		}
	}
}
