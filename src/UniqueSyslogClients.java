import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;
import java.util.TreeSet;


public class UniqueSyslogClients {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		File file  = new File("C:\\Documents and Settings\\admin\\Desktop\\test.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		
		Set<String> set = new TreeSet<String>();
		while ( (line = br.readLine()) != null){	
			int index = line.indexOf("SyslogTCPReader");
			String tName = line.substring(index,index + 18);
			set.add(tName);
		}
		
		System.out.println(set.size() + "  ,  " + set);

	}

}
