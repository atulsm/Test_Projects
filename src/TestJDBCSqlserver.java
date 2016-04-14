import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class TestJDBCSqlserver {
	
	private static final String SQLSERVER_URL_NTLMV2 = "jdbc:jtds:sqlserver://10.204.103.42:1433/AgentManager;ssl=off;domain=stldom005;useNTLMv2=true";
	private static final String SQLSERVER_URL = "jdbc:jtds:sqlserver://10.204.103.42:1433/AgentManager;ssl=off";
	
	
	public static void main(String[] args) throws Exception {
		boolean isNTLMV2 = true;
		boolean isSqlAuth = false;
		
		Connection conn = null;
		Statement stmt = null;
		
		if(isNTLMV2){
			try{
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				conn = DriverManager.getConnection(SQLSERVER_URL_NTLMV2, "administrator", "Control123");
				stmt = conn.createStatement();
				ResultSet set = stmt.executeQuery("select 1");
				System.out.println("NTLMv2 connection is successful");
				set.next();
				System.out.println(set.getString(1));
			}finally{
				stmt.close();
				conn.close();
			}
		}
		
		if(isSqlAuth){
			try{
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				conn = DriverManager.getConnection(SQLSERVER_URL, "sa", "Control123");
				stmt = conn.createStatement();
				ResultSet set = stmt.executeQuery("select 1");
				System.out.println("Sqlauth connection is successful");
				set.next();
				System.out.println(set.getString(1));
			}finally{
				stmt.close();
				conn.close();
			}
		}
		
	}

}
