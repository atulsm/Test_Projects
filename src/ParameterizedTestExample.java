import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
    
    @RunWith(Parameterized.class)
    public class ParameterizedTestExample
    {
       // Fields
       private String datum;
       private String expectedResult;
       
       /**
        \* Constructor.
        \* The JUnit test runner will instantiate this class once for every
        \* element in the Collection returned by the method annotated with
        \* @Parameters.
        */
       public ParameterizedTestExample(String datum, String expected)
       {
          this.datum = datum;
          this.expectedResult = expected;
       }
       
       /**
        \* Test data generator.
        \* This method is called the the JUnit parameterized test runner and
        \* returns a Collection of Arrays.  For each Array in the Collection,
        \* each array element corresponds to a parameter in the constructor.
        */
       @Parameters
       public static Collection<Object[]> generateData()
       {
           Object[][] array =  {
                   { "1", "v1" },
                   { "2", "v2" },
                   { "3", "v3" }
                };
    	   
          // In this example, the parameter generator returns a List of
          // arrays.  Each array has two elements: { datum, expected }.
          // These data are hard-coded into the class, but they could be
          // generated or loaded in any way you like.
          return Arrays.asList(array);
        		  

       }
       
       /**
        \* The test.
        \* This test method is run once for each element in the Collection returned
        \* by the test data generator -- that is, every time this class is
        \* instantiated. Each time this class is instantiated, it will have a
        \* different data set, which is available to the test method through the
        \* instance's fields.
        */
       @Test
       public void testWhatever()
       {
          Whatever w = new Whatever();
          String actualResult = w.doWhatever(this.datum);
          System.out.println(datum);
       }
       
       @Test
       public void testWhatever2()
       {
          Whatever w = new Whatever();
          String actualResult = w.doWhatever(this.datum);
          System.out.println(datum);
       }
    }
    
    class Whatever{

		public Whatever() {
		}

		public String doWhatever(String datum) {
			return null;
		}
    	
    }
