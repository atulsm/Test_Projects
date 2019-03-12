import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadPrint2 {

	public static void main(String[] args) {
		AtomicInteger val = new AtomicInteger(0);
		Thread t1 = new PrinterThread(val);
		Thread t2 = new PrinterThread(val);
		
		t1.start();
		t2.start();
	}

	private static class PrinterThread extends Thread{
		private AtomicInteger val;
		public PrinterThread(AtomicInteger val){
			this.val = val;
		}
		
		@Override
		public void run() {
			while(true){
				if(val.get() > 9){
					return;
				}
				
				System.out.println(val.incrementAndGet());
				synchronized (val) {
					val.notify();
					try {
						val.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
