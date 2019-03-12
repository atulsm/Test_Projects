import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

public class MultiThreadPrint {

	private static int i=0;
	public static void main(String[] args) {
		Mutex val = new Mutex();
		ExecutorService exec = Executors.newFixedThreadPool(2);
		exec.execute(new PrinterThread(val));
		exec.execute(new PrinterThread(val));
		exec.shutdown();
	}

	private static class PrinterThread implements Runnable{
		private Mutex val;
		public PrinterThread(Mutex val){
			this.val = val;
		}
		
		@Override
		public void run() {
			while(i<11){				
				try{
					val.acquire();
					System.out.println(i++);				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					val.release();
				}
			}
		}
	}
}
