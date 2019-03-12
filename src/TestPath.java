import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPath {

	public static void main(String[] args) {
		Path testFile = Paths.get("test.txt");
		try(BufferedReader br = Files.newBufferedReader(testFile)){
			String str;
			while((str = br.readLine()) != null) {
				System.out.println(str);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
