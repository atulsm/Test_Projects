import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {
	
	public static void main(String[] args) {
		final CountDownLatch startLatch = new CountDownLatch(4);
		
		new Thread() {
			public void run() {
				System.out.println("Awaiting all latches to finish counting down");
				try {
					startLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Flood gate is open");
			};
		}.start();
		
		for (int threadNo = 0; threadNo < 4; threadNo++) {
		  new Thread(){
			  public void run() {
				  try {
					startLatch.countDown();
					System.out.println("Waiting for countDown " + System.nanoTime());
				} catch (Exception e) {}
			  };			  
		  }.start();
		  
		  try {
			Thread.sleep(2000);
		  } catch (InterruptedException e) {
			e.printStackTrace();
		  }
		}
		
		
	}

}
