import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

public class TestMvel {

	public static void main(String[] args) {
		
		Employee e = new Employee();
		e.setFirstName("Atul");
		e.setLastName("Soman");
		
		Map<String, Object> input = new HashMap<>();
		input.put("employee", e);
		
		String lastName = MVEL.evalToString("employee.lastName", input);
		System.out.println(lastName);

	}
	
	private static final class Employee {
		private String firstName;
		private String lastName;

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setFirstName(String string) {
			this.firstName = string;
		}

		public void setLastName(String string) {
			this.lastName = string;
		}
		
	}

}
