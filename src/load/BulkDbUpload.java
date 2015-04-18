package load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

/**

dropTable took 37ms
createTable took 101ms
At Sat Apr 18 10:11:36 IST 2015, Inserted 10000 records
At Sat Apr 18 10:11:37 IST 2015, Inserted 20000 records
At Sat Apr 18 10:11:38 IST 2015, Inserted 30000 records
At Sat Apr 18 10:11:38 IST 2015, Inserted 40000 records
At Sat Apr 18 10:11:39 IST 2015, Inserted 50000 records
Took 4 seconds
readRecords took 148ms to read 50000 records
updateRecord took 1451ms
Took 5869 milliseconds to complete
 
 * @author Atul
 *
 */
public class BulkDbUpload {


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
		String sql1 = "INSERT INTO RAW_DATA_FILES_INFO VALUES (?,?,?,?,?,?,?,?::timestamptz,?::timestamptz)";
		String sql = "insert into data values (?,?,?,?,?,?,?,?,?)";
		long start = System.currentTimeMillis();

		PreparedStatement stmt = conn.prepareStatement(sql);
		try{
			for (int i = 0; i < count; i++) {
				stmt.setInt(1, i);
				stmt.setString(2, "data1");
				stmt.setString(3, "data2");
				stmt.setString(4, "data3");
				stmt.setString(5, "data4");
				stmt.setString(6, "data5");
				stmt.setString(7, "data6");
				stmt.setString(8, "data7");
				stmt.setString(9, "data8");
							
				stmt.addBatch();
	
				if (i % 10000 == 0) {
					stmt.executeBatch();
					System.out.println("At " + new Date()+ ", Inserted " + i + " records");
				}
			}
		} finally {
			if (stmt != null)
				stmt.executeBatch();
				stmt.close();
			if (conn != null) {
				if (!conn.getAutoCommit()) {
					conn.commit();
				}
				//conn.close();
			}
		}
		System.out.println("Took " + (System.currentTimeMillis() - start)/ 1000 + " seconds");
	}
	
	private static void insertRecord1(int count) throws SQLException {
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