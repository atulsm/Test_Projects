package eventsimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will read events.csv and generate a Map which represents that event.
 * While creating events.csv from excel, replace all , with |. 
 * 
 * @author satul
 *
 */
public class EventSimulator {
	
	private static List<String> header = new ArrayList<>();
	private static List<Map<String, String>> events = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println(getEvent(0));
	}

	public static Map<String, String> getEvent(int index){
		load();
		return events.get(index);
	}
	
	private static void load(){
		if(!events.isEmpty())
			return;
		
		File file = new File("src\\eventsimulator\\events.csv");
		if(!file.exists()){
			throw new RuntimeException(file + " do not exist");
		}	

		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String headerLine = br.readLine();
			populateHeader(headerLine);
			
			String eventLine = null;
			while((eventLine=br.readLine())!= null){
				String[] fields = eventLine.split(",");
				Map<String, String> event = new HashMap<>();
				for(int i=0;i<fields.length;i++){
					String val = fields[i];
					if(val!=null && !val.isEmpty()){
						event.put(header.get(i), val);
					}
				}
				events.add(event);
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private static void populateHeader(String headerLine){
		String[] headers = headerLine.split(",");
		for(String headerVal : headers){
			String val = headerVal.substring(headerVal.indexOf('(')+1, headerVal.length()-1);
			header.add(val);
		}
	}
}
