import java.sql.Connection;
import java.sql.Driver;
import java.sql.Statement;
import java.util.Properties;

public class TestDB {

	public static void main(String[] args) {

		try {
			Driver dr = new oracle.jdbc.OracleDriver();
			Properties props = new Properties();
			props.put("user", "esecdba");
			props.put("password", "esecdba");

			Connection c = dr.connect("jdbc:oracle:thin:@164.99.202.230:1521:XE", props);
			int i=0;
			
			while(true){
				Statement stmt = c.createStatement();
				stmt.executeQuery("select * from dual");
				stmt.close();
				System.out.println(i++);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
