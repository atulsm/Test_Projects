package dbloadtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**

dropTable took 49ms
createTable took 117ms
insertRecord took 26522ms to insert 50000 records
readRecords took 203ms to read 50000 records
updateRecord took 993ms
Took 27887 milliseconds to complete
 
 * @author Atul
 *
 */
public class SimpleDBInserts {

	private static Connection conn;
	
	private static Connection getConnection() throws SQLException {
		String url = "jdbc:postgresql://localhost/pykh";
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","atul");
		return DriverManager.getConnection(url, props);
	}
	
	public static void main(String[] args) throws Exception {
		conn = getConnection();	
		long start = System.currentTimeMillis();
		try{
			dropTable();
			createTable(); 			
			//truncateTable();
			insertRecord(50000);
			readRecords();
			updateRecord();

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.close();
		}
		System.out.println("Took " + (System.currentTimeMillis() - start) + " milliseconds to complete");
	}
	
	private static void insertRecord(int count) throws SQLException {
		long start = System.currentTimeMillis();
		Statement st = conn.createStatement();		
		for(int i=0;i<count;i++){
			st.execute("insert into data values (" + i + ",'data1','data2','data3','data4','data5','data6','data7','data8')");
		}		
		st.close();
		System.out.println("insertRecord took " + (System.currentTimeMillis() - start) + "ms to insert " + count + " records");
	}
	
	private static void updateRecord() throws SQLException {
		long start = System.currentTimeMillis();
		Statement st = conn.createStatement();
		st.execute("update data set field1='data11' where field1='data1'");
		st.close();
		System.out.println("updateRecord took " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private static void readRecords() throws SQLException {
		long start = System.currentTimeMillis();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from data where field1='data1'");
		int count = 0;
		while (rs.next())
		{
			count++;
		   //System.out.print("Column 1 returned ");
		   //System.out.println(rs.getString(1));
		} rs.close();
		st.close();
		System.out.println("readRecords took " + (System.currentTimeMillis() - start) + "ms to read " + count + " records");
	}
	
	private static void truncateTable() throws SQLException {
		long start = System.currentTimeMillis();
		Statement st = conn.createStatement();
		st.execute("truncate table data");
		st.close();
		System.out.println("truncateTable took " + (System.currentTimeMillis() - start) + "ms");
	}

	
	private static void createTable() throws SQLException {
		long start = System.currentTimeMillis();
		Statement st = conn.createStatement();
		st.execute("CREATE TABLE data (id integer PRIMARY KEY,field1 text, field2 text, field3 text, field4 text, field5 text, field6 text, field7 text, field8 text );");
		st.close();
		System.out.println("createTable took " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private static void dropTable() throws SQLException {
		long start = System.currentTimeMillis();
		try{
			Statement st = conn.createStatement();
			st.execute("drop TABLE data");
			st.close();
		}catch(Exception e){
			System.out.println("Exception while dropping table. Maybe first time? :" + e.getLocalizedMessage());
		}
		System.out.println("dropTable took " + (System.currentTimeMillis() - start) + "ms");
	}

}

