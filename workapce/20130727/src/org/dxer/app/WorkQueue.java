package org.dxer.app;

import java.util.LinkedList;

/**
 * 
 * @author user
 * 
 */
public class WorkQueue {

	private boolean Running;
	private final Worker[] workers;
	private final LinkedList<Object> queue;
	private final int threadNum;

	public WorkQueue(int threadNum) {
		Running = true;
		this.threadNum = threadNum;
		queue = new LinkedList<Object>();
		workers = new Worker[threadNum];
		for (int i = 0; i < threadNum; i++) {
			workers[i] = new Worker();
			workers[i].start();
		}
	}

	public void getState() {
		for (int i = 0; i < threadNum; i++) {
			System.out.println(workers[i].getState().name());
		}
	}

	public void execute(Object r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	public void stop() {
		Running = false;
		synchronized (queue) {
			queue.notifyAll();
		}
	}

	private class Worker extends Thread {

		@Override
		public void run() {
			Runnable r = null;
			while (Running) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							// ignored
						}
					}
					r = (Runnable) queue.removeFirst();
				}
				try {
					r.run();
					Thread.sleep(1000 * 15);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
