package org.dxer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final int SERVER_PORT = 8888;
	private static final String DIRECTORY = "D:/jar/";

	public void start() throws Exception {
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
		System.out.println("Server is started");
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println(socket.getInetAddress().getHostAddress()
					+ " is connected.");
			new Thread(new WorkThread(socket)).start();
		}
	}

	/**
	 * 多线程处理类
	 * 
	 * @author user
	 * 
	 */
	class WorkThread implements Runnable {
		private Socket socket;

		public WorkThread(Socket socket) {
			this.socket = socket;
		}

		/**
		 * 获得文件的大小
		 * 
		 * @param fileName
		 * @return
		 */
		private long getFileSize(String fileName) {
			long size = -1;
			File file = new File(DIRECTORY + fileName);
			if (!file.exists()) {
				return size;
			}
			size = file.length();
			return size;
		}

		private void sendFileSize(String message) {
			PrintWriter out = null;
			long size = -1;
			try {
				out = new PrintWriter(socket.getOutputStream());
				String[] strs = message.split(" ");
				if (strs.length == 3) {
					String fileName = strs[1];
					size = getFileSize(fileName);
					String retMsg = null;
					if (size == -1) {
						retMsg = "File isn't exists.";
					} else {
						retMsg = "Length:" + Long.toString(size);
					}
					out.println(retMsg);
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 传送文件
		 * 
		 * @param fileName
		 * @param startPos
		 * @param endPos
		 */
		private void process(String msg) {
			int length = msg.indexOf("]Range:bytes=");
			String fileName = msg.substring(1, length);
			String region = msg.split("=")[1];
			long startPos = Long.parseLong(region.split("-")[0]);
			long endPos = Long.parseLong(region.split("-")[1]);

			File file = new File(DIRECTORY + fileName);
			RandomAccessFile rf = null;
			OutputStream out = null;
			try {
				out = socket.getOutputStream();
				rf = new RandomAccessFile(file, "rw");
				rf.seek(startPos);
				int bufferSize = 1024;
				byte[] buf = new byte[bufferSize];
				int times = (int) ((endPos - startPos) / bufferSize);
				int size = (int) ((endPos - startPos) % bufferSize);
				int len = 0;
				for (int i = 0; i <= times; i++) {
					if (i == times) {
						len = rf.read(buf, 0, size);
					} else {
						len = rf.read(buf, 0, bufferSize);
					}
					if (len != -1) {
						out.write(buf, 0, len);
						out.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (rf != null) {
						rf.close();
					}
				} catch (IOException e) {
				}
			}

		}

		public void run() {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String host = socket.getInetAddress().getHostAddress();
				while (true) {
					String msg = in.readLine().trim();
					System.out.println("[" + host + "]: " + msg);
					if (msg.toLowerCase().startsWith("get ")) {
						sendFileSize(msg);
					} else if (msg.contains("]Range:bytes=")) {
						process(msg);
						System.out.println("-> " + msg + " is finished");
						break;
					} else if (msg.toLowerCase().equals("bye")) {
						System.out.println("[client:" + host + "]"
								+ " is offline");
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}

					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.start();
	}
}
