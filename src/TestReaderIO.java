import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;

public class TestReaderIO {

	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		try {
			//82289029
			File file = new File("d://test//access.log");
			System.out.println(file.length());
			Reader freader = new InputStreamReader(new FileInputStream(
					file));
			long start = System.currentTimeMillis();
			//long currpos = skip(freader, 100000);
			long currpos = freader.skip(82289029);
			System.out.println(System.currentTimeMillis() - start);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			//82289029
			File file = new File("d://test//access.log");	
			
			long start = System.currentTimeMillis();
			//long currpos = skip(freader, 100000);
			RandomAccessFile rand = new RandomAccessFile(file,"r");
			rand.seek(82289029);
			System.out.println(System.currentTimeMillis() - start);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public static long skip(Reader freader, long n) throws IOException {
		char skipBuffer[] = null;
		if (n < 0L)
			throw new IllegalArgumentException("skip value is negative");
		int nn = (int) Math.min(n, 8192);
		if ((skipBuffer == null) || (skipBuffer.length < nn))
			skipBuffer = new char[nn];
		long r = n;
		while (r > 0) {
			int nc = freader.read(skipBuffer, 0, (int) Math.min(r, nn));
			if (nc == -1)
				break;
			r -= nc;
		}
		return n - r;
	}

}
