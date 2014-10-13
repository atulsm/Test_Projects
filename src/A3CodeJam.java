import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class A3CodeJam {


	private static final String[] p = {"00","01","10","11","000","001","010","011","100","101","110","111"};
	
	private static List<Word> Length8 = new ArrayList<A3CodeJam.Word>();
	private static List<Word> Length9 = new ArrayList<A3CodeJam.Word>();
	private static List<Word> Length10 = new ArrayList<A3CodeJam.Word>();
	private static List<Word> Length11 = new ArrayList<A3CodeJam.Word>();
	private static List<Word> Length12 = new ArrayList<A3CodeJam.Word>();
	
	public static void main(String[] args) throws Exception {
		Set<Word> words = new HashSet<A3CodeJam.Word>();
		
		for(int i=0;i<p.length;i++){
			for(int j=0;j<p.length;j++){
				for(int k=0;k<p.length;k++ ){
					for (int l=0;l<p.length;l++){		
						Word word = new Word(p[i],p[j],p[k],p[l]);
						if(word.validate()){
							words.add(word);	
						}						
					}
				}
			}
		}
		
//		System.out.println(words.size() + ":" + words);
		List<Word> sort = new ArrayList<Word>(words);
		Collections.sort(sort);		
		for(int i=0;i<sort.size();i++){
			Word word = sort.get(i);
			if(word.length() ==8){
				Length8.add(word);
			}
			if(word.length() ==9){
				Length9.add(word);
			}
			if(word.length() ==10){
				Length10.add(word);
			}
			if(word.length() ==11){
				Length11.add(word);
			}
			if(word.length() ==12){
				Length12.add(word);
			}			
		}
		
		//System.out.println(Length8.size() + ":" + Length8);
//		System.out.println(Length9.size() + ":" + Length9);
//		System.out.println(Length10.size() + ":" + Length10);
		//System.out.println(Length11.size() + ":" + Length11);
//		System.out.println(Length12.size() + ":" + Length12);


		
		validate(args[0]);
		
		
	}
	
	private static class Word implements Comparable<Word>{
		public String a,b,c,d;
		public Word(String a, String b, String c, String d){
			this.a=a;this.b=b;this.c=c;this.d=d;
		}
		
		public boolean validate(){
			if(a.startsWith(b) || a.startsWith(c) || a.startsWith(d) ||
				b.startsWith(a) || b.startsWith(c) || b.startsWith(d) ||
				c.startsWith(a) || c.startsWith(b) || c.startsWith(d) ||
				d.startsWith(a) || d.startsWith(b) || d.startsWith(c)){
				return false;
			}
			
			List<String> sort = new ArrayList<String>(4);
			sort.add(a);
			sort.add(b);
			sort.add(c);
			sort.add(d);
			Collections.sort(sort);
			
			a = sort.get(0);
			b = sort.get(1);
			c = sort.get(2);
			d = sort.get(3);
			
			return true;			
		}
		
		@Override
		public int hashCode() {
			return new String(a+b+c+d).hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			Word in = (Word)obj;
			return ( (in.a == a) && (in.b==b) && (in.c==c) && (in.d==d));
		}
		
		@Override
		public String toString() {
			StringBuilder tmp = new StringBuilder();
			return tmp.append(a).append(",").append(b).append(",").append(c).append(",").append(d).toString();
		}
		
		public String actualString(){
			return a+b+c+d;
		}

		@Override
		public int compareTo(Word o) {
			return new String(a+b+c+d).compareTo(new String(o.a+o.b+o.c+o.d));
		}
		
		public int length(){
			return a.length() + b.length() + c.length() + d.length();
		}
	}
	
	public static void validate(String input){
		if(input.length() == 8){
			validate(input, Length8);
		}else if(input.length() == 9){
			validate(input, Length9);
		}else 		if(input.length() == 10){
			validate(input, Length10);
		}else 		if(input.length() == 11){
			validate(input, Length11);
		}else 		if(input.length() == 12){
			validate(input, Length12);
		}else{
			System.out.print("FALSE");
		}
	}
	
	public static void validate(String input, List<Word> list){
		for(Word word : list){
			String[] w = {word.a, word.b, word.c, word.d};

			for(int i=0;i<w.length;i++){
				for(int j=0;j<w.length;j++){
					for(int k=0;k<w.length;k++ ){
						for (int l=0;l<w.length;l++){	
							if(w[i] == w[j] ||w[i]==w[k] || w[i] == w[l] 
									|| w[j] ==w[i] || w[j]==w[k]||w[j]==w[l]||
									w[k]==w[i] ||w[k]==w[j]||w[k]==w[l]||
									w[l]==w[i]||w[l]==w[j]||w[l]==w[k]){
								continue;
							}
							
							
							Word w1 = new Word(w[i],w[j],w[k],w[l]);
							if(input.equals(w1.actualString())){
								System.out.print("TRUE");
								//System.out.println(w1);
								System.exit(0);
							}						
						}
					}
				}
			}			
			
		}
		System.out.print("FALSE");
		
	}

	


	
}
