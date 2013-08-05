package org.dxer.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class Crawler {

	WorkQueue workQueue = new WorkQueue(2);
	ThreadPool pool = new ThreadPool(2);

	BufferedWriter os = null;

	public Crawler(String urls) throws Exception {
		HttpGet httpGet = new HttpGet(urls);
		HttpClient httpClient = HttpUtils.getHttpClient(1);
		HttpResponse response = httpClient.execute(httpGet);
		// 获取网页内容
		String pageContent = IOUtils.getContent(response.getEntity()
				.getContent());

		ArrayList<String> list = HtmlUtils.getAllUrlsFromPage(pageContent);

		for (String s : list) {
			System.out.println(s);
			LinkQueue.addUnVisited(s);

		}

		// for (String url : urls) {
		// LinkQueue.addUnVisited(url);
		// }
		File file = new File("d:/tttt.txt");
		try {
			os = new BufferedWriter(new FileWriter(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		int count = 1;
		try {
			while (!LinkQueue.isUnVisitedEmpty()) {
				HttpClient httpClient = HttpUtils.getHttpClient(count++);
				String url = LinkQueue.getUnVisited();
				HttpGet httpGet = new HttpGet(url);
				Downloader downloader = new Downloader(httpClient, httpGet, os);
				workQueue.execute(downloader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// httpClient.getConnectionManager().shutdown();
		}
	}

	public void start2() {
		int count = 1;
		try {
			while (!LinkQueue.isUnVisitedEmpty()) {
				HttpClient httpClient = HttpUtils.getHttpClient(count++);
				String url = LinkQueue.getUnVisited();
				System.out.println(url);
				HttpGet httpGet = new HttpGet(url);
				Downloader downloader = new Downloader(httpClient, httpGet, os);
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
		int count = 1;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		while (!LinkQueue.isUnVisitedEmpty()) {
			HttpClient httpClient = HttpUtils.getHttpClient(count++);
			String url = LinkQueue.getUnVisited();
			System.out.println(url);
			HttpGet httpGet = new HttpGet(url);
			Downloader downloader = new Downloader(httpClient, httpGet, os);
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
		Crawler crawler = new Crawler(new String("http://bj.58.com/hezu/"));
		crawler.start();

	}
}
