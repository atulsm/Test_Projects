import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestStringEquals {

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while ( !(line = br.readLine()).equals("Exit")){ 		
			System.out.println(line);
			if(line == " "){
				System.out.println("Empty line, using = ");
			}
			
			if(line.equals(" ")){
				System.out.println("Empty line using equals");
			}
		}
		
		br.close();
	}

}
