package hdfs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsWrite101 {

	public static void main(String[] args) throws Exception {
		// hdfs://ip:port/path/to/directory

		Configuration config = new Configuration();
		config.addResource(new Path("core-site.xml"));
		config.addResource(new Path("hdfs-site.xml"));

		InputStream inputStream = new BufferedInputStream(new FileInputStream("test.txt"));

		FileSystem hdfs = FileSystem.get(new URI("hdfs://idcdvstl233:8020"), config);
		System.out.println(hdfs.getWorkingDirectory());

		FSDataInputStream inStream = hdfs.open(new Path("hdfs://idcdvstl233:8020/tmp/data.txt"));

		String str = null;
		while ((str = inStream.readLine()) != null) {
			System.out.println(str);
		}

		OutputStream outputStream = hdfs.create(new Path("hdfs://idcdvstl233:8020/tmp/test.txt"), true);

		try {
			IOUtils.copyBytes(inputStream, outputStream, 4096, false);
		} finally {
			IOUtils.closeStream(inputStream);
			IOUtils.closeStream(outputStream);
		}
	}

}
