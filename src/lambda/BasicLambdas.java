package lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BasicLambdas {

	public static void main(String[] args) {
		predicates();
		functions();
		
	}

	private static void predicates() {
		Predicate<String> strLength = (s) -> s.length() < 10;
		System.out.println(strLength.test("atul"));

		Integer[] nums = {1,3,6,10,30,100};
		List<Integer> numbers = Arrays.asList(nums);
		List<Integer> filteredList = numbers.stream().filter(i -> i>5).collect(Collectors.toList());
		System.out.println(filteredList);
	}
	

	private static void functions() {
		Function<Integer, Integer> square = new Function<Integer, Integer>() {
			@Override
			public Integer apply(Integer t) {
				return t*t;
			}
		};
		
		Function<Integer, Integer> square1 = (x) -> x*x;
		
		System.out.println(square.apply(5));
		System.out.println(square1.apply(5));
	}

}
