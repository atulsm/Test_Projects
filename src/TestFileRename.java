import java.io.File;


public class TestFileRename {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File existingTools = new File("Test.txt");
		File oldTools = new File("Test1.txt");
		File existingToolsBackup = new File("Test2.txt");
		
		existingTools.renameTo(existingToolsBackup);
		oldTools.renameTo(existingTools);
	}

}
