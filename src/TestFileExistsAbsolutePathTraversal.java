import java.io.File;


public class TestFileExistsAbsolutePathTraversal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File test = new File("h://test.txt");
		System.out.println(test.exists());
		
		File test1 = new File("h://test//..//test.txt");
		System.out.println(test1.exists());
		
		File test2 = new File("h://test2//..//test.txt");
		System.out.println(test2.exists());

	}

}
