
import java.sql.*;

class TestOracleDbConnection {
	public static void main(String args[]) {
		try {
			// step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// step2 create the connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "admin", "novell");

			// step3 create the statement object
			Statement stmt = con.createStatement();

			// step4 execute query
			ResultSet rs = stmt.executeQuery("select * from temp2");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));

			try
			{
				DatabaseMetaData	metaData = con.getMetaData();
				ResultSet			columnResults = metaData.getColumns( null, "ADMIN", "TEMP2", "%");
				
				columnResults.setFetchSize(200);
				while (columnResults.next())
				{
					String	strColumnName = columnResults.getString( 4);
					int		iColumnDataType = columnResults.getInt( 5);
					String	strColumnDataType = columnResults.getString( 6);
					int		iColumnSize = columnResults.getInt( 7);
					int		iColumnNullable = columnResults.getInt( 11);
					
					System.out.println(strColumnName+"/"+iColumnDataType+"/"+strColumnDataType);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			// step5 close the connection object
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
