public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Object obj = new Object();

		new Get(obj).start();
		new Put(obj).start();
		
	}

}
