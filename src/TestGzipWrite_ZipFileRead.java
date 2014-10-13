import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipFile;

//import com.jcraft.jzlib.GZIPOutputStream;

public class TestGzipWrite_ZipFileRead {    
    public static void main(String[] args) throws Exception {
    	String file = "compress.gz";
    	GZIPOutputStream zos = new GZIPOutputStream(new FileOutputStream(file));
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos,"UTF-8"));
    	writer.write("Test data");
    	//zos.finish();
    	writer.close();
    	ZipFile ignored = new ZipFile(file);
	}
}
