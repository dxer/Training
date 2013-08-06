package org.dxer.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

	/**
	 * 获得流中的内容
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getContent(InputStream in) throws IOException {
		BufferedReader reader = null;
		StringBuffer content = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "utf8"));
			content = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// content.append(line + "\n");
				content.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流，释放资源
			if (reader != null) {
				reader.close();
			}

			if (in != null) {
				in.close();
			}
		}
		return content.toString();
	}

	/**
	 * 将流中的文件写到文件中
	 * 
	 * @param in
	 * @param filePath
	 * @throws IOException
	 */
	public static void record2File(InputStream in, String filePath)
			throws IOException {
		record2File(in, new File(filePath));
	}

	/**
	 * 将流中的内容写到文件中
	 * 
	 * @param in
	 * @param file
	 * @throws IOException
	 */
	public static void record2File(InputStream in, File file)
			throws IOException {
		BufferedReader reader = null;
		BufferedWriter writer = null;

		try {
			reader = new BufferedReader(new InputStreamReader(in, "utf8"));
			writer = new BufferedWriter(new FileWriter(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
			}
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}

			if (writer != null) {
				writer.close();
			}
		}
	}
}
