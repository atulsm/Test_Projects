import java.util.Date;

import com.google.common.util.concurrent.RateLimiter;


public class TestRateLimiter {

	public static void main(String[] args) {
		
		Thread thread1 = new Thread(){
			public void run() {
				RateLimiter limiter = RateLimiter.create(.5);
				int i = 0;
				
				while(true){
					i++;
					
					if(i%10 == 0){
						System.out.println("Guava:" + new Date());
					}
					
					limiter.acquire();

				}
			};
		};
		
		Thread thread2 = new Thread(){
			public void run() {
				esecurity.base.util.RateLimiter limiter = new esecurity.base.util.RateLimiter(1);
				int i = 0;
				
				while(true){
					i++;
					
					if(i%10 == 0){
						System.out.println("Sentinel:" + new Date());
					}
					
					try{
						limiter.limit();
					}catch(Exception e){
						e.printStackTrace();
					}

				}
			};
		};
		
		
		
		thread1.start();
		thread2.start();
		

		
	}

}
