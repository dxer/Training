package org.dxer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	/**
	 * ��õ�ǰʱ��
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		return df.format(new Date()).toString();
	}

	/**
	 * ���client������
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
	 * �����Ϣ�����Ĳ���
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
