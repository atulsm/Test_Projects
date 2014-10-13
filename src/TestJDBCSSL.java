import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class TestJDBCSSL {


	private static final String URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=164.99.135.51)(PORT=2484))(CONNECT_DATA=(service_name=orcl)))";
	
	public static void main(String[] args) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(URL, "sentinel", "novell");
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery("select 1 from dual");
		set.next();
		System.out.println(set.getString(1));
		stmt.close();
		conn.close();
	}

}
