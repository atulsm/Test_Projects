import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class A2CodeJam {


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		List<Rule> rules = new ArrayList<A2CodeJam.Rule>();

		File file = new File(args[0]);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		String input = br.readLine();
		
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] words = line.toLowerCase().split("\\|");
			if(words.length != 4){
				System.out.println("Format not correct: " + line);
				return;
			}
			Rule rule = new Rule();
			rule.num = Integer.parseInt(words[0]);
			rule.from = words[1];
			rule.to = words[2];
			rule.left = Integer.parseInt(words[3]);
			rules.add(rule);
		}
		br.close();
		
		boolean ruleApplied = true;
		while(ruleApplied){
			ruleApplied = false;
			//stem.out.println(input);
			for(int i=0;i<rules.size();i++){
				Rule rule = rules.get(i);
				if(rule.left <=0){
					continue;
				}
				
				if(input.contains(rule.from)){
					input = input.replace(rule.from, rule.to);
					ruleApplied = true;
					break;
				}
			}			
		}
		System.out.print(input);
		
	}
	
	private static class Rule{
		public int num;
		public String from;
		public String to;
		public int left;
	}

	
}
