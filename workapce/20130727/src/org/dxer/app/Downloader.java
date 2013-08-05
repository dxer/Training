package org.dxer.app;

import java.io.BufferedWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Downloader extends Thread {

	private final HttpClient httpClient;
	private final HttpContext httpContext;
	private final HttpGet httpGet;
	// private final File file;
	private BufferedWriter os;

	public Downloader(HttpClient httpClient, HttpGet httpGet, BufferedWriter os) {
		this.httpClient = httpClient;
		this.httpGet = httpGet;
		this.httpContext = new BasicHttpContext();
		this.os = os;
	}

	@Override
	public void run() {
		try {
			HttpResponse response = httpClient.execute(httpGet);
			// 获取网页内容
			String pageContent = IOUtils.getContent(response.getEntity()
					.getContent());
			// os.write(HtmlUtils.getHouseInfo(pageContent).toString() + "\n");
			// os.flush();
			System.out.println(HtmlUtils.getHouseInfo(pageContent).toString());
			//Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}

	}
}
