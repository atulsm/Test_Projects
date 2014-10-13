import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestRegEx {
	
	public static void main(String[] args) throws Exception{		
		if(args.length != 2){
			System.out.println("Usage: java TestRegEx PATTERN FILE");
			System.exit(0);
		}
		
		File file = new File(args[1]);
		if(!file.exists()){
			System.out.println("Cannot find file: " + file);
			System.exit(0);
		}
		
		System.out.println("RegEx:" + args[0]);
		System.out.println("File :" + args[1]);
		
		Pattern pattern = Pattern.compile(args[0]);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = null;
		int linecount = 0;
		while((line = reader.readLine()) != null){
			linecount++;
			line = line.substring(20); //tipping point body starts from here
			Matcher matcher = pattern.matcher(line);	
			if(!matcher.find()){
				System.out.println("Line "+linecount+ " not matching");;
			}
			matcher.reset();
		}
		
		reader.close();
	}

}
