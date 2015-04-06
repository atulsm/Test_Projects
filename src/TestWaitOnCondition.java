import java.util.concurrent.atomic.AtomicBoolean;

public class TestWaitOnCondition {

	private AtomicBoolean isSentinelReachable = new AtomicBoolean(true);
	private final Object lock = new Object();

	private boolean checkServerReachable() {
		try {
			synchronized (lock) {
				while (!isSentinelReachable.get()) {
					lock.wait();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interupted while waiting");
		}
		return isSentinelReachable.get();
	}

	public void setServerState(boolean available) {
		synchronized (lock) {
			System.out.println("Changing server state to " + available);
			isSentinelReachable.set(available);
			lock.notify();
		}
	}

	public static void main(String[] args) {
		final TestWaitOnCondition service = new TestWaitOnCondition();
		System.out.println(service.checkServerReachable());

		service.setServerState(false);
		(new Thread() {
			@Override
			public void run() {
				service.setServerState(true);
			}

		}).start();
		System.out.println(service.checkServerReachable());

	}
}