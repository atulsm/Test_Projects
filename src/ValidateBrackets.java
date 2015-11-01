import java.util.Stack;


public class ValidateBrackets {	
	public static void main(String[] args) {
		assert validate("()");
		assert validate("(())");
		assert !validate("(()");
		assert !validate("(()");
		assert validate("(()())");
		assert !validate("((((((((");
		assert !validate(")))))))");
		assert validate("(()()(()()))");
	}

	
	public static boolean validate(String data) {
		Stack<Character> stack = new Stack<Character>();
		for(Character d : data.toCharArray()){
			if(d ==  '('){
				stack.push(d);
			}else if (d == ')'){
				if(stack.size() ==0){
					System.out.println("invalid");
					return false;
				}
				stack.pop();
			}else{
				System.out.println("Expression invalid char " + d);
				return false;
			}			
		}
		
		if(stack.size() == 0){
			System.out.println("Expression valid");
			return true;
		}else{
			System.out.println("Expression invalid");
			return false;
		}
		
	}
}
