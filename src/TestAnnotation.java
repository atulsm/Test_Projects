import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TestAnnotation {

	@Retention(RetentionPolicy.RUNTIME) //Tells compiler to retain the anotation at runtime
	@Target({ElementType.METHOD})
	public static @interface TestAnnotate {
		String value() default "defaultValue";
	}
	
	@TestAnnotate(value="SampleValue")
	public static void main(String[] args) {
		
	}

}
