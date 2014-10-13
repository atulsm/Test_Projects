
public class TestString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		String str1 = "NetIQ Identity Manager (Quartz)";
		String str2 = "NetIQ Identity Manager (Quartz)";
		String str3 = "NetIQ Identity Manager (Quartz)";
		
		System.out.println(str1.equals(str3));
		System.out.println(str1.equals(str2));
		
		for(byte b : str1.getBytes()){
			System.out.print(b+" ");
		}
		
		System.out.println();
		for(byte b : str2.getBytes()){
			System.out.print(b+" ");
		}
		
		System.out.println();
		for(byte b : str3.getBytes()){
			System.out.print(b+" ");
		}
	}

}
