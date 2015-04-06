import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



public class TestWorker {

	private LinkedBlockingQueue<String> queue;
	private ExecutorService exec;
	private static boolean isShutDown = false;
	
	private static int poolSize = 10;
	private static int addcount = 0;
	private static AtomicInteger removalcount = new AtomicInteger(0);
	private static int totalEventsToSend = 10000;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TestWorker local = new TestWorker();
		local.queue = new LinkedBlockingQueue<>(10);
		local.exec = Executors.newFixedThreadPool(poolSize);
		
		//initialize workers
		for(int i=0;i<poolSize;i++){
			Runnable worker = new EventWorker(local,local.queue);
			local.exec.execute(worker);
		}
		
		long start = System.currentTimeMillis();
		//Add events to queue
		String event;
		while(addcount < totalEventsToSend){
			event = "Event" + addcount++;
			local.queue.put(event);
		}
		
		local.waitQueueEmpty();
		isShutDown = true;
		local.exec.shutdown();
		System.out.println("Add count : " + addcount + ", Removal : " + removalcount + " took " + (System.currentTimeMillis() - start)/1000 + " seconds");
	}
	
	/**
	 * This is the final action to execute which takes 1 millisec
	 * @param input
	 * @return
	 */
	public String process(String input){
		try{
			Thread.sleep(1);
			removalcount.incrementAndGet();
			System.out.println(input);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return input;
	}
	
	private void unpack(){
		try{
			Thread.sleep(1);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	private void waitQueueEmpty(){
		while(queue.size() > 0){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class EventWorker implements Runnable{		
		private final TestWorker main;
		private final LinkedBlockingQueue<String> queue;
		
		public EventWorker(TestWorker main,LinkedBlockingQueue<String> queue){
			this.main = main;
			this.queue = queue;
		}
		
		@Override
		public void run(){
			try {
				while(!Thread.currentThread().isInterrupted()){
					String event = queue.poll(5, TimeUnit.SECONDS);									
					if(event != null){
						main.unpack();
						main.process(event);
					}else{
						//Wait to process all events in queue and then shutdown.
						if(isShutDown){
							break;
						}
					}
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}


}
