import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class TestStringSplit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		String data = ",a,b,,";
		
		//Wrong, ignoring training commas
		String[] res1 = data.split(",");
		System.out.println(res1.length);
		
		
		//Use guava
		List<String> res2 = Lists.newArrayList(Splitter.on(",").split(data));
		System.out.println(res2.size());
	}

}
