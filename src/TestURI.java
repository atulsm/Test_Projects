import java.net.URI;

public class TestURI {
	public static void main(String[] args) {
		URI resource = URI.create("/visual-analytics/proxy/kibana-int/dashboard/Alert Dashboard");
		System.out.println(resource);
	}
}
