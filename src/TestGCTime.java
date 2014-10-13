import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestGCTime {
    public static void main(String args[]) throws Exception{
        List<GarbageCollectorMXBean> l = ManagementFactory.getGarbageCollectorMXBeans();
        
        while(true){
        
	        for(int i=0;i<1000000;i++){
	        	Date c = new Date();
	        	String str = new String (new byte[10000]);
	        }
	        
	        //System.gc();
	        
	        for(GarbageCollectorMXBean b : l) {
	            System.out.println(b.getName() + ": " + b.getCollectionTime());
	        }
	        System.out.println(calculateGCTime());
	        Thread.sleep(1000);
        }
        
    }
    
    private static long calculateGCTime(){
    	long val = 0;
        List<GarbageCollectorMXBean> l = ManagementFactory.getGarbageCollectorMXBeans();
        for(GarbageCollectorMXBean b : l) {
            val += b.getCollectionTime();
        }
        return val;
    }
}