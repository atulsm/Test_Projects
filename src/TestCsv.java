import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;



public class TestCsv {

	public static void main(String[] args) throws Exception{
		
		CSVReader reader = new CSVReader(new FileReader(new File("d:\\tmp\\bugs-2014-06-24_bugs_since_2011.csv")));
		List<String[]> file1 = reader.readAll();
		reader.close();
		
		reader = new CSVReader(new FileReader(new File("d:\\tmp\\bugs-2014-06-24_closed_since_2011.csv")));
		List<String[]> file2 = reader.readAll();
		reader.close();
		
		Map<String, String> map = new HashMap<String, String>();
		for(String[] key : file2){
			map.put(key[0], key[1]);
		}
		
		
		CSVWriter writer = new CSVWriter(new FileWriter(new File("d:\\tmp\\bugs_since_2011_with_closed_date.csv")));
		String newVal = "";
		for(String[] key : file1){
			if(map.get(key[0]) != null){
				newVal = map.get(key[0]);
			}else{
				newVal = "";
			}
			
			String[] newArray = new String[key.length+1];
			System.arraycopy(key, 0, newArray, 0, key.length);
			newArray[key.length] = newVal;
			
			writer.writeNext(newArray);
		}
		writer.close();
		
	}

}
