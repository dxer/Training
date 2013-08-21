import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * ��ָ����Ŀ¼�У������ṩ���������Ұ���������jar�ļ�
 * 
 * @author walker
 * 
 */
public class FindJar {

	/* ������ҵĽ�� */
	HashMap<String, String> map = new HashMap<String, String>();
	/* Ҫ���ҵ����� */
	private String _className;

	private String toStandard(String className) {
		className = className.replace("/", ".");
		return className;
	}

	private String getClassName(String className) {
		className = className.replace("/", ".");
		className = className.replace("\\", ".");
		if (className.endsWith(".class")) {
			int len = className.lastIndexOf(".class");
			className = className.substring(0, len);
		}
		return className;

	}

	/**
	 * ����jar�ļ�����ȡ���е�class�ļ�
	 * 
	 * @param file
	 */
	private void paraseJar(File file) {
		JarEntry jarEntry = null;
		JarInputStream jin = null;
		try {
			jin = new JarInputStream(new FileInputStream(file));
			while ((jarEntry = jin.getNextJarEntry()) != null) {
				String nameStr = jarEntry.getName();
				if (nameStr.endsWith(".class")) {
					int len = nameStr.lastIndexOf(".class");
					String className = toStandard(nameStr.substring(0, len));
					if (className.equals(_className)) {
						map.put(className, file.getAbsolutePath());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (jin != null) {
					jin.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * ��������Ŀ¼
	 * 
	 * @param file
	 */
	private void search(File file) {
		if (file.isFile()) {
			paraseJar(file);
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					paraseJar(files[i]);
				} else {
					search(files[i]);
				}
			}
		}
	}

	/**
	 * ��ӡ���ҵĽ��
	 */
	private void printResult() {
		for (String key : map.keySet()) {
			System.out.println(key + " ----> " + map.get(key));
		}
	}

	/**
	 * 
	 * @param directory
	 *            Ҫ������·��
	 * @param className
	 *            Ҫ����������
	 */
	public void start(String dir, String className) {
		this._className = getClassName(className);

		File file = new File(dir);
		if (!file.exists()) {
			System.err.println(dir + " isn't exist.");
			System.exit(1);
		}
		search(file);
		printResult();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage:\n\t" + FindJar.class.getName()
					+ " <Directory/JarFile> <ClassName>");
			System.exit(0);
		}

		new FindJar().start(args[0], args[1]);
	}
}
