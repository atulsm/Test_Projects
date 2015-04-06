import java.io.File;


public class TestFreeDiskSpace {

	public static void main(String[] args) {
		File file = new File("d:");
		
		System.out.println(file.getFreeSpace());
		System.out.println(file.getTotalSpace());
		System.out.println((file.getFreeSpace()*100)/file.getTotalSpace());


	}

}
