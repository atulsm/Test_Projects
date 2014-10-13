import java.util.concurrent.Semaphore;


public class TestSemaphone {

	public static void main(String[] args) {
		final Semaphore sem = new Semaphore(1);
		int data = 0;
		
		final long events = 100000000;
		
		Thread reader = new Thread(){
			public void run() {
				int i = 0;
				try{
					while(i++ < events){
						sem.release();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			};
		};
		
		Thread writer = new Thread(){
			public void run() {
				int i = 0;
				while(i++ < events){
					try{
						sem.acquire();		
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			};
		};
		
		long start = System.currentTimeMillis();
		reader.start();
		writer.start();
		System.out.println("Took " + (System.currentTimeMillis() - start) + " ms to process " + events + " data");
	}
}
