import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class TestExecutorThreadRestart {

	private static final int THREAD_POOL_SIZE = 5;
	private static final int THREAD_POOL_QUEUE_SIZE = 100;	
	private ThreadPoolExecutor exec = new ThreadPoolExecutor(THREAD_POOL_SIZE,
			   THREAD_POOL_SIZE,
			   0L, TimeUnit.MILLISECONDS,
			   new LinkedBlockingQueue<Runnable>(THREAD_POOL_QUEUE_SIZE),
			   new ThreadPoolExecutor.CallerRunsPolicy());
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TestExecutorThreadRestart local = new TestExecutorThreadRestart();
		int totalThreads = 10;
		boolean stop = false;
		
		for(int i=0;i<totalThreads;i++){
			EventWorker worker = new EventWorker("TestExecutorThreadRestart " +i);
			local.exec.execute(worker);
			
			if(stop){
				boolean success = local.exec.remove(worker);
				if(!success){
					System.out.println("Failed to remove from queue " + i);
					worker.stopExecution();
				}
			}
		}
		
		
		local.exec.shutdown();
		local.exec.awaitTermination(30, TimeUnit.SECONDS);

	}
	
	private static class EventWorker extends Thread{
		private String name;
		private volatile boolean stopping = false;
		
		public EventWorker(String name){
			super(name);
			this.name = name;
		}
		
		public void stopExecution(){
			stopping = true;
		}

		@Override
		public void run() {
			Thread.currentThread().setName(name);
			int count=0;
			try{
				while (!stopping)
				{					
					Thread.sleep(2000);
					if(count++ > 3){
						System.out.println("Completed " + name);
						return;
					}					
				}	
				System.out.println("Stopped " + name);
			}catch(Exception e){
				e.printStackTrace();				
			}
		}
	
	}


}
