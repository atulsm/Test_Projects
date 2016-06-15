import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestConditionAwait {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("9223372036854775807");
		System.out.println(Long.MAX_VALUE);
		
		final ReentrantLock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		lock.lock();
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				lock.lock();
				System.out.println("Inner thread lock count before signal " + lock.getHoldCount());
				condition.signalAll();
				lock.unlock();
				System.out.println("Inner thread lock count " + lock.getHoldCount());
			};
		}.start();
		

		
		System.out.println("Before");
		System.out.println("Main thread lock count before await " + lock.getHoldCount());
		condition.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		System.out.println("After");
		lock.unlock();
		System.out.println("Main thread lock count " + lock.getHoldCount());
	}

}
