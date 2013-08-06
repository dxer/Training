package org.dxer.app;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class Crawler {

	WorkQueue workQueue;
	ThreadPool pool = new ThreadPool(2);

	public Crawler() {
		workQueue = new WorkQueue(3);
	}

	/**
	 * 添加url
	 * 
	 * @param urls
	 */
	public void addUrls(String[] urls) {
		for (String url : urls) {
			LinkQueue.addUnVisited(url);
		}
	}

	/**
	 * 执行任务
	 */
	public void start() {
		HttpClient httpClient = HttpUtils.getHttpClient();
		try {
			while (!LinkQueue.isUnVisitedEmpty()) {
				Server server = ProxyServer.getServer();
				HttpUtils.serProxy(httpClient, server);
				String url = LinkQueue.getUnVisited();
				HttpGet httpGet = new HttpGet(url);
				Downloader downloader = new Downloader(httpClient, httpGet);
				workQueue.execute(downloader); // 加入到工作队列
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start2() {
		try {
			while (!LinkQueue.isUnVisitedEmpty()) {
				HttpClient httpClient = HttpUtils.getHttpClient();
				String url = LinkQueue.getUnVisited();
				System.out.println(url);
				HttpGet httpGet = new HttpGet(url);
				Downloader downloader = new Downloader(httpClient, httpGet);
				pool.addTask(downloader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pool.join();
		// pool.close();
		// httpClient.getConnectionManager().shutdown();
	}

	public void start3() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		while (!LinkQueue.isUnVisitedEmpty()) {
			HttpClient httpClient = HttpUtils.getHttpClient();
			String url = LinkQueue.getUnVisited();
			System.out.println(url);
			HttpGet httpGet = new HttpGet(url);
			Downloader downloader = new Downloader(httpClient, httpGet);
			executor.execute(downloader);
		}

		executor.shutdown();

		while (!executor.isTerminated()) {

		}
		System.out.println("Finished all Theads");
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
		crawler.addUrls(urlList.toArray(new String[0]));
		crawler.start();

	}
}
