
public class TestPadding {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(String.format("%02d", 1));
		System.out.println(String.format("%02d", 0));
		System.out.println(String.format("%02d", 11));
		System.out.println(String.format("%02d", 111));
		System.out.println(String.format("%02d", -1));
		System.out.println(String.format("%02d", 1111));
	}

}
