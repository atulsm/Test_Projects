import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class CalculateTotalMem {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		BufferedReader br = null;
		try{
		File file = new File("Test.txt");
		br = new BufferedReader(new FileReader(file));
		
		String line = null;
		long count = 0;
		while((line = br.readLine())!= null){
			String lines = line.substring(28).trim();
			try{
				count += Long.parseLong(lines.substring(0, lines.indexOf(' ')));
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println(line);
			}
		}

		System.out.println(count);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			br.close();
		}
	}

}
