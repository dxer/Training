package org.dxer.app;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpUtils {

	/**
	 * 产生HttpClient对象
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient(int seed) {

		int size = ProxyServer.serverList.size();
		Server server = null;
		if (size > 0 && seed > 0) {
			server = ProxyServer.serverList.get((size - 1) % seed);
			System.out.println(server.ip + ":" + server.port);
		} else {
			return null;
		}
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");

		ThreadSafeClientConnManager clientmanager = new ThreadSafeClientConnManager();
		clientmanager.setMaxTotal(20);
		HttpClient client = new DefaultHttpClient(clientmanager, params);
		HttpHost proxy = new HttpHost(server.ip, server.port);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		return client;
	}
}
