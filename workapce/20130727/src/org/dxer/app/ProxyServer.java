package org.dxer.app;

import java.util.ArrayList;
import java.util.Random;

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
		serverList.add(new Server("210.101.131.232", 8080));
		serverList.add(new Server("210.101.131.231", 8080));
		serverList.add(new Server("202.116.160.89", 80));
		serverList.add(new Server("120.85.140.123", 9000));
		serverList.add(new Server("210.14.143.53", 7620));
		// serverList.add(new Server("125.39.66.155", 80));
	}

	/**
	 * 从列表中随机取出一个代理服务器
	 * 
	 * @return
	 */
	public static Server getServer() {
		Server server = null;
		int size = 1;
		for (Server s : serverList) {
			if (s.getState()) {
				size++;
			}
		}
		Random random = new Random();
		while (size > 0) {
			int n = random.nextInt();
			int index = Math.abs(n % size);
			server = serverList.get(index);
			if (server.getState()) {
				break;
			}
		}
		System.err.println(server.ip + ":" + server.port);
		return server;
	}

	public static void main(String[] args) {
		for (int i = 0; i < serverList.size(); i++) {
			// Server server = serverList.get(i);
			// System.out.println(server.ip + ":" + server.port);
			getServer();
		}
	}
}

class Server {
	public final String ip;
	public final int port;
	private boolean available;

	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
		available = true;
	}

	/**
	 * 获得服务器的状态
	 * 
	 * @return true 可用 false 不可用
	 */
	public boolean getState() {
		return this.available;
	}

	/**
	 * 设置服务器的状态
	 * 
	 * @param available
	 */
	public void setState(boolean available) {
		this.available = available;
	}

}
