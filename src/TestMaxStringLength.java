
public class TestMaxStringLength {

	public static void main(String[] args) {
		byte[] data = new byte[524288];
		
		System.out.println("start");
		String test = new String(data, 0, 5005);
		System.out.println("end");
		
		System.out.println(test);

	}

}
