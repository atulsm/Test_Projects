import java.util.concurrent.SynchronousQueue;

/**
 * Two threads waits and notifies other to proceed
 * Expected behavior: A B A B A B
 * 
 * @author SAtul
 *
 */
public class TestThreadNotifyUsingSynchronousQueue {
	static SynchronousQueue<String> sync = new SynchronousQueue<String>();
	
	public static void main(String[] args) throws Exception{
		while(true) {
			new Thread(()->{
				try {
					sync.put("A");
					System.out.println("A");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
	
			new Thread(()->{
				try {
					sync.take();
					System.out.println("B");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
