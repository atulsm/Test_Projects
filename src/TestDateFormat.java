import java.text.SimpleDateFormat;
import java.util.Date;


public class TestDateFormat {

	private static final SimpleDateFormat agentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
	
	private static final Object lock = new Object();
	
//	static{
//		agentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
//	}
	
	public static void main(String[] args) {
//		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		for (int i=0;i<10;i++){
			
			new Thread(){
				public void run(){
					while(true){
						try{
							synchronized (lock) {
								//Date date = agentDateFormat.parse("20110724143607.0");
								//System.out.println(date);
								System.out.println(agentDateFormat.format(new Date()));
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			
		}


	}

}
