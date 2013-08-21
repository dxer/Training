package org.dxer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

	/* 客户端列表 */
	ArrayList<Socket> clientList = new ArrayList<Socket>();
	/* 端口号 */
	public static final int PORT = 10001;

	public static void main(String[] args) {
		// 在main函数中，启动服务器
		new ChatServer().start();
	}

	public void start() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server is started...");
			Socket socket = null;
			while ((socket = server.accept()) != null) {
				clientList.add(socket);
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " connected to the server");
				new WorkThread(socket).start();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public class WorkThread extends Thread {
		private Socket socket;

		public WorkThread(Socket client) {
			this.socket = client;
		}

		@Override
		public void run() {
			DataOutputStream output = null;
			DataInputStream input = null;
			try {
				String msg = null;
				String message = null;
				input = new DataInputStream(socket.getInputStream());
				while (true) {
					msg = input.readUTF();
					System.out.println(msg);
					if (msg.trim().endsWith("]$ bye")) {
						System.out.println(socket.getInetAddress()
								.getHostAddress() + " is exited");
						output = new DataOutputStream(socket.getOutputStream());
						output.writeUTF("bye");
						clientList.remove(socket);
						break;
					}
					message = "[" + Util.getTime() + " "
							+ Util.getClientName(msg) + "]$ "
							+ Util.getContent(msg);
					for (int i = 0; i < clientList.size(); i++) {
						Socket s = clientList.get(i);
						if (s != socket) {
							output = new DataOutputStream(s.getOutputStream());
							output.writeUTF(message);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (input != null) {
						input.close();
					}

					if (output != null) {
						output.close();
					}

					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
				}
			}
		}
	}

}