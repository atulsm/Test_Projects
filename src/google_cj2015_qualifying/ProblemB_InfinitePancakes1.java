package google_cj2015_qualifying;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 
 Problem B. Infinite House of Pancakes
Confused? Read the quick-start guide.
Small input
9 points	
You may try multiple times, with penalties for wrong submissions.
Large input
12 points	
You must solve the small input first.
You have 8 minutes to solve 1 input file. (Judged after contest.)
Problem

At the Infinite House of Pancakes, there are only finitely many pancakes, but there are infinitely many diners who would be willing to eat them! When the restaurant opens for breakfast, among the infinitely many diners, exactly D have non-empty plates; the ith of these has Pi pancakes on his or her plate. Everyone else has an empty plate.

Normally, every minute, every diner with a non-empty plate will eat one pancake from his or her plate. However, some minutes may be special. In a special minute, the head server asks for the diners' attention, chooses a diner with a non-empty plate, and carefully lifts some number of pancakes off of that diner's plate and moves those pancakes onto one other diner's (empty or non-empty) plate. No diners eat during a special minute, because it would be rude.

You are the head server on duty this morning, and it is your job to decide which minutes, if any, will be special, and which pancakes will move where. That is, every minute, you can decide to either do nothing and let the diners eat, or declare a special minute and interrupt the diners to make a single movement of one or more pancakes, as described above.

Breakfast ends when there are no more pancakes left to eat. How quickly can you make that happen?
Input

The first line of the input gives the number of test cases, T. T test cases follow. Each consists of one line with D, the number of diners with non-empty plates, followed by another line with D space-separated integers representing the numbers of pancakes on those diners' plates.
Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is the smallest number of minutes needed to finish the breakfast.
Limits

1 ≤ T ≤ 100.
Small dataset

1 ≤ D ≤ 6.
1 ≤ Pi ≤ 9.
Large dataset

1 ≤ D ≤ 1000.
1 ≤ Pi ≤ 1000.
Sample

Input
  	
Output
 

3
1
3
4
1 2 1 2
1
4

	

Case #1: 3
Case #2: 2
Case #3: 3

In Case #1, one diner starts with 3 pancakes and everyone else's plate is empty. One optimal strategy is:

Minute 1: Do nothing. The diner will eat one pancake.

Minute 2 (special): Interrupt and move one pancake from that diner's stack onto another diner's empty plate. (Remember that there are always infinitely many diners with empty plates available, no matter how many diners start off with pancakes.) No pancakes are eaten during an interruption.

Minute 3: Do nothing. Each of those two diners will eat one of the last two remaining pancakes.

In Case #2, it is optimal to let the diners eat for 2 minutes, with no interruptions, during which time they will finish all the pancakes.

In Case #3, one diner starts with 4 pancakes and everyone else's plate is empty. It is optimal to use the first minute as a special minute to move two pancakes from the diner's plate to another diner's empty plate, and then do nothing and let the diners eat for the second and third minutes. 
  
 * @author user
 *
 */
public class ProblemB_InfinitePancakes1 {

		private static int T = 0;
		private static PrintWriter out = null;

		public static void main(String[] args) throws Exception{
			List<String> input = readFile("src/google_cj2015/inputb");		
			out = new PrintWriter(new BufferedWriter(new FileWriter("out.txt")));
			
			T = Integer.parseInt(input.get(0));
			for(int i=1,j=1;j<=T;i+=2,j++){
				process(input.get(i),input.get(i+1),j);
			}
			
			out.close();
			System.exit(0);
		}
		
		private static void process(String dinerCount,String in, int t){
			String[] ins = in.split(" ");

			List<Integer> diners = new ArrayList<>();
			for(String diner : ins){
				diners.add(Integer.parseInt(diner));
			}		
			
			int pass = pass(diners,0,true);		
			out.println("Case #"+t+": " + pass);		
		}
		
		/**
		 * Two options 1) Reduce every thing, 2) Move
		 * Recursivily evaluate these options and return the best.
		 * 
		 * @return
		 */
		private static int pass(List<Integer> diners, int pass,boolean print){					
			int pos = findLargest(diners, false);
			int largest = diners.get(pos);
			
			List<Integer> moveList = new ArrayList<>(diners);
			if(largest > 3){
				int newLarge = largest/2;
				moveList.set(pos, newLarge);
				moveList.add(largest - newLarge);
			}else{
				if(print){
					System.out.println(pass+largest);
				}
				return pass + largest;
			}
			
			List<Integer> dinersCopy = new ArrayList<>(diners);
			List<Integer> moveListCopy = new ArrayList<>(moveList);
			
			findLargest(dinersCopy, true);
			dinersCopy = new ArrayList<>(dinersCopy);
			
			int eatpass = pass(dinersCopy,pass+1,false);
			int movepass = pass(moveListCopy,pass+1,false); 
			
			if(movepass < eatpass){
				if(print){
					System.out.println("Move: " + diners);
					moveListCopy = new ArrayList<>(moveList);
					pass(moveListCopy,pass+1,true);
				}
				return movepass;
			}else{
				if(print){
					System.out.println("eat: " + diners);
					pass(dinersCopy,pass+1,true);
				}
				return eatpass;
			}
		}
		
		/**
		 * Eat and return the largest left
		 */
		private static int findLargest(List<Integer> diners,boolean reduce){
			int largest = 0;
			int pos = 0;
			
			for(int i=0;i<diners.size();i++){
				Integer din = diners.get(i);
				
				if(din > largest){
					largest = din;
					pos = i;
				}
				
				if(reduce){
					diners.set(i, --din);
				}
			}
			return pos;
		}
		
		private static boolean shouldPass(List<Integer> diners){
			for(Integer din : diners){
				if(din > 0){
					return true;
				}
			}
			return false;
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

	}
