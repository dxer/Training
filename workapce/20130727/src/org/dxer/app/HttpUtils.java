package org.dxer.app;

import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpUtils {

	/**
	 * 产生HttpClient对象
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry);
		// 设置最大连接数
		cm.setMaxTotal(200);

		// 设置头信息,模拟浏览器
		ArrayList<BasicHeader> heads = new ArrayList<BasicHeader>();
		heads.add(new BasicHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36"));

		params.setParameter(ClientPNames.DEFAULT_HEADERS, heads);

		HttpClient client = new DefaultHttpClient(cm, params);
		return client;
	}

	/**
	 * 设置代理服务器
	 * 
	 * @param httpClient
	 * @param server
	 */
	public static void serProxy(HttpClient httpClient, Server server) {
		if (server != null) {
			HttpHost proxy = new HttpHost(server.ip, server.port);
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
	}

	public static Server getProxy(HttpClient httpClient) {
		HttpHost proxy = (HttpHost) httpClient.getParams().getParameter(
				ConnRoutePNames.DEFAULT_PROXY);

		Server server = new Server(proxy.getHostName(), proxy.getPort());
		return server;

	}

	public static void main(String[] args) {
		HttpClient client = getHttpClient();
		Server server = ProxyServer.serverList.get(0);
		serProxy(client, server);

		System.out.println(getProxy(client).ip);

	}
}
