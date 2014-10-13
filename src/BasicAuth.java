import sun.misc.BASE64Encoder;


public class BasicAuth {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new BASE64Encoder().encode("admin:novell".getBytes() ));

	}

}
