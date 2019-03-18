
public class TestThreadLocal {
	public static ThreadLocal<String> user = new ThreadLocal<String>() {
		protected String initialValue() {
			return "Atul";
		};
	};

	public static class Run implements Runnable {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + " : " + user.get());
			user.set("Atul Soman");
			f1();
		}

		private void f1() {
			System.out.println(user.get());
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		user.set("Main Thread");
		System.out.println(Thread.currentThread().getName() + " : " + user.get());
		new Thread(new Run()).start();
		Thread.sleep(1000);
		System.out.println(Thread.currentThread().getName() + " : " + user.get());
	}

}
