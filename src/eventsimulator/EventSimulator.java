package eventsimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
	private static final Calendar cal = Calendar.getInstance();
		
	
	public static void main(String[] args) {
		Map<String, String> evt = getEvent(0);
		System.out.println(evt);
		System.out.println(ObjectSerializer.getBytes(evt).length);
		
	}

	public static Map<String, String> getEvent(int index){
		load();
		
		Map<String, String> evt = events.get(index);
		String val = evt.get("dt");
		if(val != null){
			cal.setTimeInMillis(System.currentTimeMillis()-330*60*1000);
			val = dateFormat.format(cal.getTime());
			//System.out.println(val);
			evt.put("dt", val);
		}
		
		UUID uuid = UUID.randomUUID();
		evt.put("id", uuid.toString());		
		
		return evt;
	}
	
	private static void load(){
		if(!events.isEmpty())
			return;
		
		String fileName = System.getProperty("eventFile");
		if(fileName == null){
			fileName = "src\\eventsimulator\\events.csv";
		}
		
		File file = new File(fileName);
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
					String fieldHeader = header.get(i);
					String val = fields[i];
					if(val!=null && !val.isEmpty()){					
						event.put(fieldHeader, val);						
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
