
public class TestThreadName {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		A thread = new TestThreadName.A();
		Thread t = new Thread(thread);
		t.start();
	}
	
	static class A implements Runnable{
		@Override
		public void run() {
			Thread.currentThread().setName("Hi");
			System.out.println("In run");			
		}
	}

}
