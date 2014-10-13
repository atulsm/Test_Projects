import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

import agent.JavaAgent;

public class TestJDBCClassLoading {

	public static void main(String[] args) {
		//Instrumentation in = JavaAgent.getInstrumentation();
		//Class[] before = in.getAllLoadedClasses();
		//List<Class> beforeList = Arrays.asList(before);

		try {
			URLClassLoader ucl = new URLClassLoader(new URL[] { new URL(
					"jar:file:C:\\Users\\satul\\Desktop\\test\\jconn3-6.0.jar!/") }, null);
			Driver driver = (Driver) Class.forName(
					"com.sybase.jdbc3.jdbc.SybDriver", true, ucl).newInstance();
			DriverManager.registerDriver(driver);

			driver.getClass();
			driver.getClass().getName();
			
			Connection conn =DriverManager.getConnection("jdbc:sybase:Tds:164.99.96.234:2638?ServiceName=zenworks_zone12_may2");


			//Class[] after = in.getAllLoadedClasses();
			//List<Class> afterList = Arrays.asList(after);

	/*		for (Class c : afterList) {
				if (!beforeList.contains(c))
					System.out.println(c);
			}
			*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
