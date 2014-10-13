import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class TestChinese {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = new String("\u91CD\u8BD5\u6B21\u6570".getBytes(),Charset.forName("GBK"));
		System.out.println(str);
		System.out.println("\u91CD\u8BD5\u6B21\u6570");
		
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Test.txt")),Charset.forName("GBK")));
		
			System.out.println(br.readLine());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
