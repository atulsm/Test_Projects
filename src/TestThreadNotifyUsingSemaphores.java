import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Two threads waits and notifies other to proceed
 * Expected behavior: A B A B A B
 * 
 * @author SAtul
 *
 */
public class TestThreadNotifyUsingSemaphores {
	
	static volatile Semaphore A = new Semaphore(1);
	static volatile Semaphore B = new Semaphore(1);

	
	public static class Run implements Runnable {
		private boolean isRead;

		public Run(boolean isRead) {
			this.isRead=isRead;
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					//Thread.sleep(10);
					if(isRead) {
						B.acquire();
						System.out.println(Thread.currentThread().getName());
						A.release();
					}else {
						A.acquire();
						System.out.println(Thread.currentThread().getName());
						B.release();
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public static void main(String[] args) {
		
		Thread t1 = new Thread(new Run(false), "A");
		Thread t2 = new Thread(new Run(true), "B");

		t1.start();
		t2.start();
		
	}

}
