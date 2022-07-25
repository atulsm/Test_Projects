import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

public class TestMvelComplex {

	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("mid", "sellerApp");
		map.put("weight", "501.0");
		map.put("zone", "Local");

		//Object res = MVEL.eval("mid=='sellerApp' && zone=='Local'", map);
		Object res = MVEL.eval("(weight <=  500) ?  34 : (34 + 20*(weight/500))", map); //(weight <=  500) ?  34 : (34 + 20*(weight/500 - 1)))
		System.out.println(res);

	}

}
