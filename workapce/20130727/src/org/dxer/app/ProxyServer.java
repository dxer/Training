package org.dxer.app;

import java.util.ArrayList;

/**
 * 设置代理服务器
 * 
 * @author walker
 * 
 */
public class ProxyServer {

	public static ArrayList<Server> serverList = new ArrayList<Server>();

	static {
		serverList.add(new Server("110.4.24.170", 80));
		serverList.add(new Server("110.4.24.170", 81));
		serverList.add(new Server("110.4.24.170", 82));
		serverList.add(new Server("110.4.24.170", 83));
		serverList.add(new Server("58.64.157.148", 8888));
		serverList.add(new Server("202.96.155.251", 8888));
		serverList.add(new Server("58.250.87.121", 81));
		serverList.add(new Server("114.80.136.179", 1280));
		serverList.add(new Server("101.226.74.168", 8081));
	}

	public static void main(String[] args) {
		for (int i = 0; i < serverList.size(); i++) {
			Server server = serverList.get(i);
			System.out.println(server.ip + ":" + server.port);
		}
	}
}

class Server {
	public final String ip;
	public final int port;

	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
}
