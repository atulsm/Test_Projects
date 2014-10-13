import java.util.List;


public class TestNPE {
	public static List<String> test;


	public static void main(String[] args) {
		test.get(0);
	}

}
