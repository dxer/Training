package org.dxer.app;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * 工作队列
 * 
 * @author user
 * 
 */
public class WorkQueue {

	private boolean Running;
	private final Worker[] workers;

	// 已访问队列
	private Set<String> visited = new HashSet<String>();

	// 未访问队列
	private Queue<String> unVisited = new PriorityQueue<String>();

	public WorkQueue(int threadNum) {
		Running = true;
		workers = new Worker[threadNum];
		for (int i = 0; i < threadNum; i++) {
			HttpClient httpClient = HttpUtils.getHttpClient();
			Server server = ProxyServer.getServer();
			HttpUtils.serProxy(httpClient, server);
			workers[i] = new Worker(httpClient);
			workers[i].setName("Thread - " + i);
			workers[i].start();
		}
	}

	public void stop() {
		synchronized (unVisited) {
			unVisited.notify();
			unVisited.clear();
		}
		Running = false;
	}

	public void addTask(String url) {
		synchronized (unVisited) {
			if (url != null && !url.trim().equals("") && !visited.contains(url)
					&& !unVisited.contains(url))
				unVisited.add(url);
			unVisited.notify();
		}
	}

	public void reAddTask(String url) {
		addTask(url);
	}

	/**
	 * 工作线程
	 * 
	 * @author user
	 * 
	 */
	private class Worker extends Thread {

		private HttpClient httpClient;
		private final HttpContext httpContext;

		public Worker(HttpClient httpClient) {
			this.httpClient = httpClient;
			this.httpContext = new BasicHttpContext();
		}

		@Override
		public void run() {
			HttpGet httpGet = null;
			while (Running) {
				synchronized (unVisited) {
					while (unVisited.isEmpty()) {
						try {
							unVisited.wait();
						} catch (InterruptedException e) {
						}
					}

					if (!Running) {
						return;
					}

					String url = unVisited.poll();
					httpGet = new HttpGet(url);
				}

				process(httpClient, httpGet);
			}
			httpClient.getConnectionManager().shutdown();
		}

		private void process(HttpClient httpClient, HttpGet httpGet) {
			HttpResponse response = null;
			String url = httpGet.getURI().toString();
			try {
				response = httpClient.execute(httpGet, httpContext);
			} catch (Exception e) {
				synchronized (unVisited) {
					// 访问失败,得将url重新加入到未访问列表中
					reAddTask(url);
					// 修改代理服务器
					HttpUtils.serProxy(httpClient, ProxyServer.getServer());
				}
			}

			try {
				if (response != null) {
					// 获取网页内容
					String pageContent = IOUtils.getContent(response
							.getEntity().getContent());
					System.out.println(pageContent);
				}
			} catch (Exception e) {
				e.printStackTrace();
				httpGet.abort();
			} finally {
				httpGet.releaseConnection();
			}
			visited.add(url);
			System.out.println("--------" + url);
		}
	}

}
