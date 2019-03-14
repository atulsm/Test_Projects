package functional.programming;

/**
 A function is a higher order function if at least one of the following conditions are met:

    The function takes one or more functions as parameters.
    The function returns another function as result.

 * @author SAtul
 *
 */
public class HigherOrderFunction1 {

	public static void main(String[] args) {
		HigherOrderFunction1 main = new HigherOrderFunction1();
		IFactory<String> func = main.createFactory(() -> "Producer", (c) -> System.out.println("Configuring " + c));
		func.create();
	}

	public <T> IFactory<T> createFactory(IProducer<T> producer, IConfigurator<T> configurator) {
		return () -> {
			T instance = producer.produce();
			configurator.configure(instance);
			return instance;
		};
	}

}

interface IFactory<T> {
	T create();
}

interface IProducer<T> {
	T produce();
}

interface IConfigurator<T> {
	void configure(T t);
}