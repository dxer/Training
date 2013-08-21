package org.dxer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	/**
	 * 获得当前时间
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date()).toString();
	}

	/**
	 * 获得client的名字
	 * 
	 * @param message
	 * @return
	 */
	public static String getClientName(String message) {
		String name = null;
		int len = message.indexOf("]$ ");
		name = message.substring(1, len);
		return name;
	}

	/**
	 * 获得消息的正文部分
	 * 
	 * @param message
	 * @return
	 */
	public static String getContent(String message) {
		String content = null;
		int len = message.indexOf("]$ ");
		content = message.substring(len + 3);
		return content.trim();
	}
}
