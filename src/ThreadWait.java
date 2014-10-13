

public class ThreadWait {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		A thread = new ThreadWait.A();
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
			synchronized(this){
			boolean is=false;
			try{
				wait();
				while(true){
					i++;
					if(!is)
					 is = Thread.currentThread().isInterrupted();
					
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			}
		}
	}

}

