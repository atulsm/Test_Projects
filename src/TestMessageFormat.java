import java.text.MessageFormat;

public class TestMessageFormat {

	public static final String COMMAND = "{0} || {1}";
	
	public static void main(String[] args) {
		System.out.println(MessageFormat.format(COMMAND, "ps -ef","grep java"));
	}

}
