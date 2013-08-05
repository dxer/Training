public class Get extends Thread {

	private Object lock;

	public Get(Object lock) {
		this.lock = lock;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			synchronized (lock) {
				lock.notify();
				System.out.println("get: " + i);
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
