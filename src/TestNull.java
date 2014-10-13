

public class TestNull {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TreeSet<String> set = new TreeSet<String>();
		//HashSet<String> set = new HashSet<String>();
		//set.contains(null);
		Object obj = null;
		if(obj instanceof String){
			System.out.println(true);
		}else{
			System.out.println(false);
			System.out.println((Boolean)obj);
		}

	}

}
