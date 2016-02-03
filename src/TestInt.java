
public class TestInt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Integer a = 1000;
		Integer b = 1000;
		
		System.out.println(a==b);
		a=b=1;
		System.out.println(a==b);
		
		String str = (String) null;
		System.out.println(str);
		System.out.println((String)null);
		System.out.println(Integer.MAX_VALUE);
		
	}

}
