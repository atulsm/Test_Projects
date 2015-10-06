import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {
	
	public static void main(String[] args) {
		final CountDownLatch startLatch = new CountDownLatch(1);
		for (int threadNo = 0; threadNo < 4; threadNo++) {
		  new Thread(){
			  public void run() {
				  try {
					startLatch.await();
					System.out.println(System.nanoTime());
					
				} catch (Exception e) {}
			  };			  
		  }.start();
		}
		startLatch.countDown();
	}

}
