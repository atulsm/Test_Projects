import java.util.concurrent.locks.ReentrantReadWriteLock;


public class TestReentrantReadWriteLock {
	static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public static void main(String[] args) throws Exception {	
		
		new ReadLock().start();
		new ReadLock().start();
		Thread.sleep(2000);
		System.out.println("After small sleeping ..");
		lock.writeLock().lock(); 

		System.out.println("Test");
	}
	
	static class ReadLock extends Thread{
		@Override
		public void run() {
			lock.readLock().lock();
			try{
				System.out.println("Sleeping ..");
				Thread.sleep(5000);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			lock.readLock().unlock();
		}
	}

}
