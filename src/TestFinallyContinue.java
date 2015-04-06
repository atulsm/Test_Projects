import java.util.concurrent.atomic.AtomicInteger;


public class TestFinallyContinue {

	public static void main(String[] args) {

		AtomicInteger count = new AtomicInteger(0);
		
		for(int i=0;i<10;i++){
			try{
				//do something
				continue;
			}finally{
				count.incrementAndGet();
			}
		}
		
		System.out.println(count);

	}

}
