import java.io.File;


public class TestExists {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File test = new File(args[0]);
		System.out.println("File " + args[0] + " exists:" + test.exists());

	}

}
