import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class TestJDBCSSL {


	private static final String ORACLE_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=164.99.135.51)(PORT=2484))(CONNECT_DATA=(service_name=orcl)))";
	private static final String POSTGRE_URL = "jdbc:postgresql://164.99.87.246:5432/SIEM?ssloff=true";
	private static final String SQLSERVER_URL = "jdbc:jtds:sqlserver://10.204.103.42:1433/AgentManager;ssl=off;domain=stldom005;useNTLMv2=true";
	
	
	public static void main(String[] args) throws Exception {
		boolean isOracle = false;
		boolean isPostgres = false;
		boolean isSQLServer = true;
		
		if(isOracle){
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(ORACLE_URL, "sentinel", "novell");
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery("select 1 from dual");
			set.next();
			System.out.println(set.getString(1));
			stmt.close();
			conn.close();
		}
		
		if(isPostgres){
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection(POSTGRE_URL, "sentinel", "novell");
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery("select 1 from dual");
			set.next();
			System.out.println(set.getString(1));
			stmt.close();
			conn.close();
		}
		
		if(isSQLServer){
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Connection conn = DriverManager.getConnection(SQLSERVER_URL, "administrator", "Control123");
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery("select 1");
			set.next();
			System.out.println(set.getString(1));
			stmt.close();
			conn.close();
		}
	}

}
