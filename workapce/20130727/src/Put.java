public class Put extends Thread {

	private Object lock;

	public Put(Object lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			synchronized (lock) {
				lock.notify();
				System.out.println("put: " + i);
				try {
					if (i < 999) {
						lock.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
