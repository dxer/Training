package org.dxer.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Downloader extends Thread {

	private final HttpClient httpClient;
	private final HttpContext httpContext;
	private final HttpGet httpGet;

	public Downloader(HttpClient httpClient, HttpGet httpGet) {
		this.httpClient = httpClient;
		this.httpGet = httpGet;
		this.httpContext = new BasicHttpContext();
	}

	@Override
	public void run() {
		HttpResponse response = null;
		String url = httpGet.getURI().toString();
		try {
			response = httpClient.execute(httpGet, httpContext);
		} catch (Exception e) {
			// 访问失败,得将url重新加入到未访问列表中
			LinkQueue.addUnVisited(url);
		}

		try {
			if (response != null) {
				// 获取网页内容
				String pageContent = IOUtils.getContent(response.getEntity()
						.getContent());
				System.out.println(pageContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			httpGet.abort();
		} finally {
			httpGet.releaseConnection();
		}
		LinkQueue.addVisited(url);
	}
}
