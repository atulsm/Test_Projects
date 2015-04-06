
public class TestFreeMemory {

	public static void main(String[] args) {
		System.out.println("FreeMemory: " +  (Runtime.getRuntime().freeMemory()/1000000) + " mb");
		System.out.println("MaxMemory: " +  (Runtime.getRuntime().maxMemory()/1000000) + " mb");

	}
	

}
