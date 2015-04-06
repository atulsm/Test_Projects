import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import com.esecurity.util.logging.ExceptionHandler;


public class TestRunTime {

	public static void main(String[] args) {

		InputStream reader = null;
		Process processOnLinux = null;
		
		try {
			String[] cmds = new String[2];
			cmds[0] = "ping";
			cmds[1] = "164.99.175.165";

			processOnLinux = Runtime.getRuntime().exec(cmds);
			reader = new BufferedInputStream(processOnLinux.getInputStream());
			StringBuilder buffer = new StringBuilder();
			for (;;) {
				int charCount = reader.read();
				if (charCount == -1)
					break;
				buffer.append((char) charCount);
			}
			String outputText = buffer.toString();
			System.out.println(outputText);

		} catch (Exception ex) {
			ex.printStackTrace();
			if(processOnLinux != null){
				processOnLinux.destroy();
			}
		} finally {
			processOnLinux = null;
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}

		
	}

}
