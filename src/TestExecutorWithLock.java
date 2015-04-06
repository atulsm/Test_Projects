import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;



public class TestExecutorWithLock {

	private static int count = 0;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	private EventWorker worker = new EventWorker();

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestExecutorWithLock local = new TestExecutorWithLock();
		
		while(true){
			local.worker.setEvent("event" + count++);
			local.exec.execute(local.worker);	
			try{
				Thread.sleep(5);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	
	private static class EventWorker implements Runnable{
		private String event;
		Semaphore lock = new Semaphore(1);
		
		public void setEvent(String event){
			try{
				lock.acquire();
				this.event = event;
			}catch(InterruptedException e){
				
			}
		}	
		
		public void run() {
			System.out.println(event);
			lock.release();
			try{
				Thread.sleep(5);
			}catch(Exception e){
				e.printStackTrace();
			}		
		}
	
	}


}
