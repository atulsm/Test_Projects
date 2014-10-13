import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class TestCountDownLatch {

	private static final CountDownLatch latch = new CountDownLatch(1);
	private static CyclicBarrier barrier = new CyclicBarrier(2);

	public static void main(String[] args) throws Exception{

		System.out.println("Before Thread initialization");
		new Thread(){
			
			public void run(){
				while(true){
					System.out.println("Bbefore await");
					try{
						barrier.await();
					}catch(Exception e){
						e.printStackTrace();
					}
					System.out.println("After await");
				}
			};
			
		}.start();
		
		Thread.sleep(5000);
		System.out.println("After Sleep, about to call reset");
		barrier.await();
		barrier.reset();
		Thread.sleep(10);
		System.out.println("Done");
	}

}
