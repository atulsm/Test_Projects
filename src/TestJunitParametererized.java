import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * @author satul
 *
 */
@RunWith(value = Parameterized.class)
public class TestJunitParametererized {
	private int size;

	 public TestJunitParametererized(int number) {
	   this.size = number;
	 }

	 @Parameters
	 public static Collection data() {
	   Object[][] data = new Object[][] { { 0 }, { 1 }, { 2 }};
	   return Arrays.asList(data);
	 }

	    
	@Test
	public void sampleTest() {
		System.out.println("Testing with size " + size);    	
	}		
    
	private String getName(){
		switch(size){
			case 0:
				return "Minimal";
			case 1:
				return "Average";
			case 2:
				return "FullyLoaded";				
		}
		return "default";
	}
	
    
}
