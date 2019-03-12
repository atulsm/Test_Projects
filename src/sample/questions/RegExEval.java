package sample.questions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegExEval {
	private static String[] lines = {"julia@hackerrank.com","julia_@hackerrank.com","julia_0@hackerrank.com","julia0_@hackerrank.com","julia@gmail.com"};
	
	public static void main(String[] args) throws Exception{		
		
		Pattern pattern = Pattern.compile("[a-z]{1,6}_?[0-9]{0,4}\\@hackerrank\\.com");
		for(String line:lines){
			Matcher matcher = pattern.matcher(line);	
			if(!matcher.find()){
				System.out.println("Line "+line+ " not matching");;
			}else{
				System.out.println("Line "+line+ " matching");;
			}
			matcher.reset();
		}
		
	}

}
