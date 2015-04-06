import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class TestCGFileIntegrity {

	private static final int FILE_COUNT =100;
	private static final int LINES =100;
	
	private static final File parentFolder = new File("D:\\cg\\filemonitor");
	
	public static void main(String[] args) throws Exception {
		for(int i=0;i<FILE_COUNT;i++){
			new Worker("File"+i+".txt").start();
		}		
	}
	
	private static class Worker extends Thread{
		private final String fileName;
		public Worker(String fileName){
			this.fileName = fileName;
		}
		
		public void run() {
			try{				
				for(int j=0;j<LINES;j++){
					File file = new File(parentFolder, fileName);
					BufferedWriter br = new BufferedWriter(new FileWriter(file,true));
					br.write("\nLine " + j);
					br.close();
					Thread.sleep(30000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
