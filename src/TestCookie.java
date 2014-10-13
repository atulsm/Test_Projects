
public class TestCookie {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String cookie = "JSESSIONID=A9D5C5F0C4CDD21EDA6442D7E8520409; Path=/svem; Secure";
		String cookieValue = cookie.substring(0, cookie.indexOf(";"));
		System.out.println(cookieValue + ".");

	}

}
