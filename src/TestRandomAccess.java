import java.io.File;
import java.io.RandomAccessFile;


public class TestRandomAccess {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		File file = new File("d:\\test\\swms.exe.Config");
		if(!file.exists()){
			System.out.println("File doesnt exist");
			return;
		}

		RandomAccessFile config = new RandomAccessFile(file,"rw");
		
		String data = null;
		long fp = config.getFilePointer();
		while((data = config.readLine())!=null){
			System.out.println(fp + ":" + data);
			if(data.contains("address=\"tcp://127.0.0.1:1024\"")){
				data = data.replace("tcp://127.0.0.1:1024", "tcp://127.0.0.1:" + 1025);
				config.seek(fp);
				config.writeBytes(data);
				config.close();
				break;
			}
			fp = config.getFilePointer();
		}
		
	}

}
