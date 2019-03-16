
public class TestGeneric {

	public static class GenericClass<T> {
		public T getValue() {
			return null;
		}
	}
	
	public static <T,K> T getValue(K input) {
		return (T)input;
	}
	
	public static void main(String[] args) {
		System.out.println("" + TestGeneric.getValue("Hi"));
	}

}
