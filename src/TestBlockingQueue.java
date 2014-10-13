import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;


public class TestBlockingQueue {

	public static void main(String[] args) {
		final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);
		final long events = 100000000;
		
		Thread reader = new Thread(){
			public void run() {
				int i = 0;
				try{
					while(i++ < events){
						queue.take();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			};
		};
		
		Thread writer = new Thread(){
			public void run() {
				int i = 0;
				while(i++ < events){
					try{
						queue.put(i);					
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			};
		};
		
		long start = System.currentTimeMillis();
		reader.start();
		writer.start();
		System.out.println("Took " + (System.currentTimeMillis() - start) + " ms to process " + events + " data");
	}
	
}
