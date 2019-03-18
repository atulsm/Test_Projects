
/**
 * Two threads waits and notifies other to proceed
 * Expected behavior: A B A B A B
 * 
 * @author SAtul
 *
 */
public class TestThreadNotify {
	
	private static class Monitor {
		private boolean notified = false;
		private Object lock = new Object();
		
		public void waitLock() throws Exception{
			synchronized(lock) {
				if(!notified) {
					lock.wait();
				}
				notified=false;
			}
		}
		
		public void notifyLock() {
			synchronized(lock) {
				notified=true;
				lock.notify();
			}
		}
		
	}
	
	public static class Run implements Runnable {
		private Monitor monitor;
		private boolean wait;

		public Run(Monitor monitor, boolean wait) {
			this.monitor=monitor;
			this.wait=wait;
		}
		
		@Override
		public void run() {
			while(true) {
				synchronized (monitor) {
					if(wait) {
						try {
							monitor.waitLock();
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName());
					}else {
						monitor.notifyLock();
						System.out.println(Thread.currentThread().getName());
					}
				}
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static void main(String[] args) {
		Monitor monitor = new Monitor();
		
		Thread t1 = new Thread(new Run(monitor, false), "A");
		Thread t2 = new Thread(new Run(monitor, true), "B");

		t1.start();
		t2.start();
		
	}

}
