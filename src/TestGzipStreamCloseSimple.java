import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipFile;

//import com.jcraft.jzlib.GZIPOutputStream;

public class TestGzipStreamCloseSimple {

	BufferedWriter writer = null;
	GZIPOutputStream zos = null;
	private static String file = "compress1.zip";

	public void init() {
		try {
			zos = new GZIPOutputStream(new FileOutputStream(new File(file),
					true));
			writer = new BufferedWriter(new OutputStreamWriter(zos,"UTF-8"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void write(String json) {
		if (writer == null) {
			init();
		}

		try {
			writer.write(json, 0, json.length());
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean close() {
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
				//zos.finish();
				//zos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	
    private static boolean isValid(File file) {
        try (ZipFile ignored = new ZipFile(file)) {
            return true;
        } catch (IOException e) {
        	e.printStackTrace();
        	return false;
        }
    }
    
    public static void main(String[] args) throws Exception {
    	TestGzipStreamCloseSimple obj = new TestGzipStreamCloseSimple();
    	obj.write("Test data");
    	obj.close();
    	//Thread.sleep(10000);
    	
    	File file1 = new File(file);
    	System.out.println(isValid(file1));
	}

}
