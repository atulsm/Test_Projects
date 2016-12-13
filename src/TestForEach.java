import java.util.ArrayList;
import java.util.List;

public class TestForEach {

	public static void main(String[] args) {
		List<String> vals 	= new ArrayList<String>();
		vals.add("a");
		vals.add("b");
		vals.add("c");
		
		for(String val:vals){
			System.out.println(val);
			vals.remove(val);
			break;
		}
		
		System.out.println(vals);
	}

}
