package org.dxer.app;

import java.util.LinkedList;

/**
 * 工作队列
 * 
 * @author user
 * 
 */
public class WorkQueue {

	private final Worker[] workers;
	private final LinkedList<Object> queue;

	public WorkQueue(int threadNum) {
		queue = new LinkedList<Object>();
		workers = new Worker[threadNum];
		for (int i = 0; i < threadNum; i++) {
			workers[i] = new Worker();
			workers[i].setName("Thread - " + i);
			workers[i].start();
		}
	}

	public void execute(Object r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class Worker extends Thread {

		@Override
		public void run() {
			Runnable r = null;
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
						}
					}
					r = (Runnable) queue.removeFirst();
				}
				try {
					r.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
