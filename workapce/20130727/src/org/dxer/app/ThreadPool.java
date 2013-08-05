package org.dxer.app;

import java.util.LinkedList;

public class ThreadPool extends ThreadGroup {
	private boolean isClosed = false;
	private LinkedList<Runnable> taskQueue;
	private static int threadPoolId;
	private int threadId;

	public ThreadPool(int size) {
		super("Thread pool - " + threadPoolId++);
		setDaemon(true);

		taskQueue = new LinkedList<Runnable>();
		for (int i = 0; i < size; i++) {
			new WorkThread().start();
		}
	}

	/**
	 * 执行任务线程
	 * 
	 * @author user
	 * 
	 */
	private class WorkThread extends Thread {
		public WorkThread() {
			super("Work thread - " + threadId++);
		}

		public void run() {
			while (!isInterrupted()) {
				Runnable task = null;

				try {
					task = getTask();
				} catch (InterruptedException e) {
				}

				if (task == null) {
					return;
				}

				task.run();
			}
		}
	}

	protected synchronized Runnable getTask() throws InterruptedException {
		while (taskQueue.size() == 0) {
			if (isClosed) {
				return null;
			}
			wait();
		}
		return taskQueue.removeFirst();
	}

	public synchronized void addTask(Runnable task) {
		if (isClosed) {
			throw new IllegalStateException();
		}

		if (task != null) {
			taskQueue.add(task);
			notify();
		}
	}

	public void join() {
		synchronized (this) {
			isClosed = true;
			notifyAll();
		}

		Thread[] threads = new Thread[activeCount()];
		int count = enumerate(threads);
		for (int i = 0; i < count; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void close() {
		if (!isClosed) {
			isClosed = true;
			taskQueue.clear();
			interrupt();
		}
	}

}
