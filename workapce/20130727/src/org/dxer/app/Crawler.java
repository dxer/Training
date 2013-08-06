package org.dxer.app;

import java.util.ArrayList;

public class Crawler {

	WorkQueue workQueue = null;

	public Crawler() {
		if (workQueue == null) {
			workQueue = new WorkQueue(3);
		}
	}

	/**
	 * 执行任务
	 * 
	 * @param urls
	 *            要处理的任务
	 */
	public void start(String[] urls) {
		for (String url : urls) {
			workQueue.addTask(url);
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ArrayList<String> urlList = new ArrayList<String>();
		for (int i = 0; i < 250; i += 25) {
			urlList.add("http://movie.douban.com/top250?start=" + i);
		}

		Crawler crawler = new Crawler();
		crawler.start(urlList.toArray(new String[0]));

	}
}
