
public class TestUTF16 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		byte[] bytes = "hello".getBytes("UTF-16LE");
		for(byte b : bytes)
			System.out.println((int)b);		
	}

}
