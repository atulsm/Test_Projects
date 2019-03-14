package functional.programming;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BasicLambdas {

	public static void main(String[] args) {
		//Predicate, represents a simple function that takes a single value as parameter, and returns true or false
		predicates();
		
		//The Function interface represents a function (method) that takes a single parameter and returns a single value
		functions();
		
		//The Java UnaryOperator interface is a functional interface that represents an operation which takes a single parameter and returns a parameter of the same type
		unary();
		
		//The Java BinaryOperator interface is a functional interface that represents an operation which takes two parameters and returns a single value. Both parameters and the return type must be of the same type.
		binary();
		
		//The Java Supplier interface is a functional interface that represents an function that supplies a value of some sorts. The Supplier interface can also be thought of as a factory interface.
		supplier();
		
		//The Java Consumer interface is a functional interface that represents an function that consumes a value without returning any value. 
		consumer();
		
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
	

	private static void unary() {
		UnaryOperator<Integer> u = (i -> i+1);
		System.out.println(u.apply(3));
	}		
	
	private static void binary() {
		BinaryOperator<Integer> binary = ( (i,j) -> i+j);
		System.out.println(binary.apply(3,4));
	}		
	
	private static void supplier() {
		Supplier<Integer> random = ( () -> new Random().nextInt());
		System.out.println(random.get());
	}
	
	private static void consumer() {
		Consumer<Integer> consume = (x -> System.out.println(x));
		consume.accept(3);
	}
	
}
