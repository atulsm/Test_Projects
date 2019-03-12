import org.junit.Assert;

public class TestQueryStringValReplace {	
	public static String replace(String queryString, String propName, String newVal) {
		return queryString.replaceAll(propName+"=[^&]+", propName+"=" + newVal);
	}

	public static void main(String[] args) {
		Assert.assertEquals("supplyId=newVal&search=true", replace("supplyId=oldVal&search=true", "supplyId","newVal"));
		Assert.assertEquals("supplyId=newVal", replace("supplyId=oldVal", "supplyId","newVal"));
		Assert.assertEquals("search=true&supplyId=newVal&3rdprop=val", replace("search=true&supplyId=oldVal&3rdprop=val", "supplyId","newVal"));
		
		Assert.assertEquals("search=true&3rdprop=val", replace("search=true&3rdprop=val", "supplyId","newVal"));
	}
}
