package functional.programming;

import java.util.stream.IntStream;

public class ThreadLambda {

	public static void main(String[] args) {
		IntStream.range(1, 10)
			.forEach((i) -> {
				new Thread(()-> {System.out.println(i);}).start();
			});
	}
}
