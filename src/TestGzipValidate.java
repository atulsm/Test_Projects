import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class TestGzipValidate {
	public static void main(String[] args) throws Exception {
		System.out.println(isValidGzip(new File("d://25-1134.gz")));
		System.out.println(isValidGzip(new File("d://25-1134.bad.gz")));
		System.out.println(isValidGzip(new File(
				"d://Sentinel Anomaly Engine 5-16-13 2.41 PM.mov")));
	}

	/**
	 * Gzip is valid if you can read till the end without exception
	 * 
	 */
	private static boolean isValidGzip(File file) {
		System.out.println("Checking if file is valid for "
				+ file.getAbsolutePath());
		try (GZIPInputStream zis = new GZIPInputStream(
				new FileInputStream(file));
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(zis));) {

			while (reader.readLine() != null) {
				// just try to read till end with out exception
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
