import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class TestGzip {
	
	public static void main(String[] args) {
		//read from stdin - compress - write to stdout - uncompress and write to stdout
		try{
			GZIPOutputStream gout = new GZIPOutputStream(new FileOutputStream(new File("c://compress.txt")));
			gout.write("hi".getBytes());
			gout.close();
			
			GZIPInputStream gin = new GZIPInputStream(new FileInputStream(new File("c://compress.txt")));
			InputStreamReader in = new InputStreamReader(gin);
			System.out.println(new BufferedReader(in).readLine());
			in.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
