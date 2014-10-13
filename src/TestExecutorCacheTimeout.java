import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;



public class TestExecutorCacheTimeout {

	private static int count = 0;
	private ExecutorService exec = Executors.newCachedThreadPool();
	private EventWorker worker = new EventWorker();

	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TestExecutorCacheTimeout local = new TestExecutorCacheTimeout();
		
		while(true){
			local.worker.setEvent("event" + count++);
			Future<String> future = local.exec.submit(local.worker);
			System.out.println(future.get(5, TimeUnit.SECONDS));		
			
			Thread.sleep(90000);
		}

	}
	
	private static class EventWorker implements Callable<String>{
		private String event;
		
		public void setEvent(String event){
			this.event = event;
		}	


		@Override
		public String call() throws Exception {
			Thread.sleep(4000);
			return event;
		}
	
	}


}
