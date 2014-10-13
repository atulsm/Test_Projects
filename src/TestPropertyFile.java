import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


public class TestPropertyFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties props = new Properties();
		File file = new File("obj-component.JasperReportingComponent.properties");
		try{
			props.load(new FileInputStream(file));
			
			for(String name: props.stringPropertyNames()){
				System.out.println(name + ":" + props.getProperty(name));
			
			
				if("reporting.csv.outputdir".equals(name)){
					props.setProperty(name, "dummy");
				}
			}
			props.store(new FileOutputStream(file), "");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
