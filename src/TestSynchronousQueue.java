import java.util.concurrent.SynchronousQueue;


public class TestSynchronousQueue {

	public static void main(String[] args) {
		final SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
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
