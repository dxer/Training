package org.dxer;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClient {
	private DataOutputStream output;
	private DataInputStream input;
	/* 服务器端口号 */
	public static final int PORT = 10001;
	/* 客户端名称 */
	private String clientName;

	public ChatClient(String clientName) {
		this.clientName = clientName;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage:\n\t" + ChatClient.class.getName()
					+ " <ClientName>");
			System.exit(1);
		}
		// 连接服务器
		new ChatClient(args[0]).connect("127.0.0.1");
	}

	public void connect(String host) {
		Socket socket = null;
		try {
			socket = new Socket(host, PORT);
			System.out.println("Connected to server");
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			ReadThread readThread = new ReadThread();
			WriteThread writeThread = new WriteThread();
			readThread.start();
			writeThread.start();

			readThread.join();
			writeThread.join();
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

	public class ReadThread extends Thread {

		public void run() {
			String msg = null;
			try {
				while (true) {
					msg = input.readUTF();
					if (msg != null) {
						if (!msg.equals("bye")) {
							System.out.println(msg);
						} else {
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class WriteThread extends Thread {

		public void run() {
			try {
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in));
				String message = null;
				while (true) {
					System.out.print("> ");
					message = stdIn.readLine();
					output.writeUTF("[" + clientName + "]$ " + message);
					if (message.equals("bye")) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}