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
	private int size1;
	private int size2;

	 public TestJunitParametererized(int number1, int number2) {
	   this.size1 = number1;
	   this.size2 = number2;
	 }

	 @Parameters
	 public static Collection data() {
	   Object[][] data = new Object[][] { { 0,1 }, { 1,2 }, { 2,3 }};
	   return Arrays.asList(data);
	 }

	    
	@Test
	public void sampleTest() {
		System.out.println("Testing with size1 " + size1 + " size2 " + size2);    	
	}		
    
	private String getName(){
		switch(size1){
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
