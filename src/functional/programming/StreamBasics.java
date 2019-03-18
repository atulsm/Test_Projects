package functional.programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class StreamBasics {

	public static void main(String[] args) {
		List<String> stringList = new ArrayList<String>();
		
		stringList.add("ONE");
        stringList.add("TWO");
        stringList.add("THREE");

        stringList
        	.stream()
        	.parallel()
        	.map((s) -> s.toLowerCase())
        	//.forEach((s) -> System.out.println(s));
        	.forEachOrdered((s) -> System.out.println(s));

        stringList.add("One flew over the cuckoo's nest");
    	stringList.add("To kill a muckingbird");
    	stringList.add("Gone with the wind");

    	stringList
    		.stream()
    		.flatMap((value) -> {
    			String[] split = value.split(" ");
    			return Arrays.asList(split).stream();
    		})
    		.forEach((value) -> System.out.println(value));
    	
    	Optional<String> reduced = stringList
        		.stream()
        		.reduce((value, combinedValue) -> {
        			return combinedValue + " + " + value;
        		});

    	System.out.println(reduced.get());
    	
    	System.out.println(stringList.toArray());
    	System.out.println(stringList.toArray(new String[] {}));
    	
    	IntStream.range(1,10).forEach(e->System.out.println(e));

	}

}
