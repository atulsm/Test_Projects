import java.util.concurrent.CyclicBarrier;


public class TestCyclicBarrier {

	private static CyclicBarrier barrier = new CyclicBarrier(2, ()-> {System.out.println("All threads have arrived");});

	public static void main(String[] args) throws Exception{
		new Thread(()-> {
			try {
				System.out.println("Thread 1, awaiting to complete");
				Thread.sleep(10000);
				barrier.await();
				System.out.println("Thread 1, awaite complete");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(()-> {
			try {
				System.out.println("Thread 2, awaiting to complete");
				Thread.sleep(5000);
				barrier.await();
				System.out.println("Thread 2, awaite complete");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}).start();

		System.out.println("Done");
	}

}
