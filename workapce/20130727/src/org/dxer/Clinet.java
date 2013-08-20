package org.dxer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;

public class Clinet {
	private static final int SERVER_PORT = 8888;
	private String host;
	private int threadNum;

	/**
	 * 
	 * @param host
	 *            主机ip
	 * @param threadNum
	 *            线程数
	 */
	public Clinet(String host, int threadNum) {
		this.host = host;
		this.threadNum = threadNum;
	}

	private String get(String inputStr, int pos) {
		String fileName = null;
		if (inputStr.trim().toLowerCase().startsWith("get ")) { // 暂时只支持路径中没有空格
			String[] strs = inputStr.split("\\s{1,}");
			if (strs.length == 3) {
				fileName = strs[pos];
			}
		}
		return fileName;
	}

	public void start() {
		BufferedReader reader = null;
		Socket socket = null;
		PrintWriter writer = null;
		BufferedReader in = null;
		try {
			socket = new Socket(host, SERVER_PORT);
			System.out.println("Connetced to server...");
			writer = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.print("> ");
				String msg = reader.readLine().trim();
				String fileName = get(msg, 1);
				String srcFileName = get(msg, 2);
				writer.println(msg);
				writer.flush();
				if (msg.equals("bye")) {
					break;
				}

				String retMsg = in.readLine();
				System.out.println(retMsg);
				if (retMsg != null && retMsg.length() > 0) {
					if (retMsg.equals("File isn't exists.")) {
						System.out.println("File isn't exists.");
					} else {
						long size = Long.parseLong(retMsg.split(":")[1]);
						File file = new File(fileName);
						RandomAccessFile accessFile = new RandomAccessFile(
								file, "rwd");
						accessFile.setLength(size);
						accessFile.close();
						// 将文件按照设置的线程数目进行分块, block为每一块的大小
						int block = (int) ((size % threadNum == 0) ? (size / threadNum)
								: (size / threadNum + 1));

						for (int i = 0; i < threadNum; i++) {
							Socket s = new Socket(host, SERVER_PORT);
							new Thread(new WorkThread(s, file, srcFileName,
									block, i)).start();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}

				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
			}
		}
	}

	class WorkThread implements Runnable {
		private Socket socket;
		private File file;
		private String fileName;
		private int block;
		private int threadId;

		public WorkThread(Socket socket, File file, String fileName, int block,
				int threadId) {
			this.socket = socket;
			this.file = file;
			this.fileName = fileName;
			this.block = block;
			this.threadId = threadId;
		}

		@Override
		public void run() {
			int startPos = threadId * block; // start postation
			int endPos = (threadId + 1) * block - 1; // end postation
			PrintWriter writer = null;
			InputStream in = null;
			RandomAccessFile accessFile = null;
			try {
				in = socket.getInputStream();
				writer = new PrintWriter(socket.getOutputStream());
				accessFile = new RandomAccessFile(file, "rwd");
				accessFile.seek(startPos);
				writer.println("[" + fileName + "]Range:bytes=" + startPos
						+ "-" + endPos);
				writer.flush();

				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					accessFile.write(buf, 0, len);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}

					if (writer != null) {
						writer.close();
					}
					if (accessFile != null) {
						accessFile.close();
					}

					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Clinet clinet = new Clinet("127.0.0.1", 3);
		clinet.start();
	}

}
