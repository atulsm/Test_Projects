
public class TestThreadInterrupt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		A thread = new TestThreadInterrupt.A();
		Thread t = new Thread(thread,"TestThread");
		t.start();
		try{
		 Thread.sleep(5000);
		}catch (Exception e) {
			e.printStackTrace();
		}
		t.interrupt();
		
	}
	
	static class A implements Runnable{
		@Override
		public void run() {
			int i=0;
			boolean is=false;
			Object lock = new Object();
			try{
				while(true){
					synchronized (lock) {
						lock.wait();
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
