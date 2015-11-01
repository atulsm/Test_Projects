package google_cj2015_r1;

/**
Problem

Your publishing house has decided to use monkeys randomly typing at keyboards to write great works of literature. You are the supervisor for one monkey with a keyboard containing K keys, each of which is labeled with an uppercase English letter. (There may be multiple keys displaying the same letter.) The monkey will start with an empty string and repeat the following S times: choose a key from its keyboard uniformly at random and press it, adding a copy of that key's letter to the right end of the string. The final resulting string will have length S.

You have a target word of length L that you are hoping the monkey will type. (The target word will not necessarily be a real English word.) This target word may even appear multiple times in what the monkey types. (Overlapping instances count too -- for example, if "ABA" is the target word and the monkey types "ABABA", that contains two instances of the target.)

You plan to pay the monkey one banana for each instance of the target word that it types. When you go to inspect the monkey's work, you will bring along the minimum number of bananas that you need to ensure that you will always have enough bananas to pay the monkey, no matter what it has typed. Then, you will pay the monkey one banana for each instance of the target word that it actually typed. You will keep the remaining bananas that you brought with you.

What is the expected number of bananas that you will get to keep?
Input

The first line of the input gives the number of test cases, T. T test cases follow. Each consists of three lines. The first contains three space-separated positive integers: K, L, and S. The second contains a string of K uppercase English letters representing the monkey's keyboard. The third contains a string of L uppercase English letters representing the target word.
Output

For each test case, output one line containing "Case #x: y", where y is the expected number of bananas you will get to keep after paying the monkey.
y will be considered correct if it is within an absolute or relative error of 10-6 of the correct answer. See the FAQ for an explanation of what that means, and what formats of real numbers we accept.

Limits

1 ≤ T ≤ 100.
Small dataset

1 ≤ K ≤ 7.
1 ≤ L ≤ S ≤ 7.
Large dataset

1 ≤ K ≤ 100.
1 ≤ L ≤ S ≤ 100.
Sample

Input
  	
Output
 

5
7 6 6
BANANAS
MONKEY
2 3 4
AA
AAA
2 1 2
AB
B
6 2 2
GOOGLE
GO
26 11 100
ABCDEFGHIJKLMNOPQRSTUVWXYZ
ROSENCRANTZ

	

Case #1: 0.0
Case #2: 0.0
Case #3: 1.0
Case #4: 0.8888889
Case #5: 9.0

Note that Case #5 is not within the limits for the Small dataset.

In Case #1, the monkey has no chance of typing the target word "MONKEY" even once (because his keyboard lacks most of the letters in "MONKEY"), so you do not bring any bananas along when you visit, and of course you do not pay any. Poor monkey!

In Case #2, the monkey is guaranteed to type "AAAA", which has two overlapping instances of the target word "AAA". You will bring two bananas and then pay both.

In Case #3, the monkey will produce the following outputs with equal probability (1/4 each): "AA", "AB", "BA", "BB". These have 0, 1, 1, and 2 instances of the target word, respectively. You must bring 2 bananas to be ready for the "BB" case, but you will on average pay (0 + 1 + 1 + 2) / 4 = 1.

In Case #4, the monkey has a 1/3 chance of typing a "G" first and a 1/3 chance of typing an "O" second, for a 1/9 chance of typing "GO". You will bring one banana and give it up 1/9 of the time.

In Case #5, the monkey could in theory type "ROSENCRANTZ" up to nine times, but the chances of this happening even once are so small that they are negligible compared to the acceptable margin of error for answers.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProblemA_TypeWriterMonkey {
	
	private static int T = 0;
	private static PrintWriter out = null;
	static final BlockingQueue queue = new ArrayBlockingQueue(1000);
	
	static float best = 0;
	static float totalAttempts = 0;
	static float totalStrings = 0;
	static String targetVal;

	public static void main(String[] args) throws Exception{
		Consumer consumer = new Consumer(queue);
		
		List<String> input = readFile("src/google_cj2015_r1/inputa");		
		out = new PrintWriter(new BufferedWriter(new FileWriter("out.txt")));
		
		T = Integer.parseInt(input.get(0));
		int next=1;
		for(int i=1;i<=T;i++){
			process(input.get(next++),input.get(next++),input.get(next++),i);
		}
		
		out.close();
		System.exit(0);
	}
	
	private static void process(String numbers,String keyboard, String target,int t){
		//System.out.println(numbers+ " " + keyboard + " " + target);
		String[] kls = numbers.split(" ");
		
		best = 0;
		totalAttempts = 0;
		totalStrings = 0;
		targetVal = target;
		
		generateAllWords(keyboard,Integer.parseInt(kls[2]));
		
		System.out.println(target + " " + best + " " + totalAttempts + " " + totalStrings + " " );
		
		float res = 0;
		if(totalStrings > 0){
			res = (best-totalStrings/totalAttempts);
		}
		
		System.out.println(target + " " + best + " " + totalAttempts + " " + totalStrings + " " + res);
		
		out.println("Case #"+t+": "+res);		
	}
	
	private static void generateAllWords(String keyboard, int targetLength){
		char[] keys = keyboard.toCharArray();

		Stack<Integer> stack = new Stack<Integer>();
		for(int i=0;i<targetLength;i++){
			stack.push(0);
		}
		
		try{
			while(true){
				generate(stack,keys,targetLength);
			}
		}catch(EmptyStackException e){
			
		}
		
	}
	
	private static void generate(Stack<Integer> stack, char[] keys, int targetLength){
		int c = stack.peek();
		if(stack.size() == targetLength){
			calculate(stack,keys,targetLength);
			c = stack.pop();
		}
				
		int next = next(c,keys);
		while( next == -1){
			c = stack.pop();
			next = next(c,keys);
			
			if(next!=-1){
				stack.push(next);
				next=0;
				//push the rest of values as zero
				for(int i=stack.size()+1;i<targetLength;i++){
					stack.push(0);
				}
			}
			
		}
		stack.push(next);
		if(stack.size() > targetLength){
			int i = targetLength;
		}
	}
	
	private static int next(int c,char[] keys){
		if(c<keys.length-1){
			return ++c;
		}		
		return -1;
	}
	
	private static void calculate(Stack<Integer> stack,char[] keys,int targetLength){
		char[] ct = new char[targetLength];
		int i=0;
		for(Integer pos : stack){
			//System.out.print(pos);
			ct[i++] = keys[pos];
		}
		
		String res = new String(ct);		
		int countMatches = countMatches(res, targetVal);
		
		//System.out.println(res + "  " + countMatches );
		
		totalAttempts++;
		totalStrings+=countMatches;
		
		if(countMatches > best){
			best = countMatches;
		}
		
		
	}
	
	private static int countMatches(String str, String findStr) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {
			lastIndex = str.indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}
	
	
	private  static final List<String> readFile(String fileName) throws Exception{
		List<String> ret = new ArrayList<String>();
		File file = new File(fileName);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String in = null;
		while ((in=br.readLine())  != null){
			ret.add(in);
		}		
		br.close();
		
		return ret;
	}


private static class Consumer implements Runnable{

    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println("Consuming " + queue.take());
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}	
	
}

