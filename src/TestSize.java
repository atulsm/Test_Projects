import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class TestSize {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String a = "hi";
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("c:/testSize"));
			out.writeObject(a);
			out.flush();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
