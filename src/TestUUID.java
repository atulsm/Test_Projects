import java.util.UUID;

public class TestUUID {

	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();

		System.out.println(uuid.toString());
		System.out.println(new StringBuilder(uuid.toString()).reverse().toString());
		
	}

}
