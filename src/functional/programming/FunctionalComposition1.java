package functional.programming;

import java.util.function.Function;

public class FunctionalComposition1 {

	public static void main(String[] args) {
		Function<Integer, Integer> add = (x) -> x + (x/2);
		Function<Integer, Integer> del = (x) -> x - (x/2);
		Function<Integer, Integer> mul = (x) -> x * x;

		System.out.println(add.compose(del).compose(mul).apply(4)); // 12
		System.out.println(add.andThen(del).andThen(mul).apply(4)); // 9

		
	}
	
}
