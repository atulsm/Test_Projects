import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class SyslogSimForAD {	
	private static final int EPS = 2000;
	private static final int NUM_THREADS = 1; 
	private static final int NUM_ES_PER_COLLECTOR = 1;

	//private static final String SERVER = "localhost";
	private static final String SERVER = "164.99.175.165";
	private static final int PORT = 1469;
	private static final boolean ssl = false;
	
	private static final String[] msg = {
		"MSWinEventLog	0	Security	1	Thu May 21 11:22:00 2009	593	Security	SYSTEM	User	Success Audit	TRAINING-NVLL2	Detailed Tracking  		A process has exited:     Process ID: 1588     Image File Name: C:\\Program Files\\Snare\\SnareCore.exe     User Name: TRAINING-NVLL2$     Domain: APPLABS     Logon ID: (0x0,0x3E7)    	0", //Windows collector
		//"%ASA-6-302014: Teardown TCP connection 7177 for management:164.99.17.109/42257 to NP Identity Ifc:164.99.17.6/443 duration 0:00:00 bytes 2698 TCP FINs"
	};
	private static int threadNum =1;
	
	public static void main(String[] args) throws Exception{
		for(int i=0;i<NUM_THREADS;i++){
			Thread.currentThread().sleep(100);
			new Thread(){
				public void run(){
					String message = msg[threadNum %(msg.length)];
					int local = threadNum++;

					try{
						Socket soc = null; 

						if(ssl){
							soc = getSSLSocket();
						}else{
							 soc = new Socket(SERVER,PORT);
						}
						PrintWriter writer = new PrintWriter(soc.getOutputStream());
						
						long time = System.currentTimeMillis();
						int localCount = 0;
						while(true){
							for(int j=0;j<NUM_ES_PER_COLLECTOR;j++){
								writer.write("EventSource"+local+"."+j+" " +message + "\n");
								writer.flush();
							}
							if(writer.checkError()){
								break;
							}
							count ++;
							localCount++;
							
							long time1 = System.currentTimeMillis();

							//check if 1 sec has passed
							if((time1-time) >= 1000){
								time = System.currentTimeMillis();
								localCount=0;
							}else if(localCount > EPS){
								sleep(1000 - (time1 - time));
								localCount = 0;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					threadNum--;
				}
			}.start();			
		}		
	}
	
	private static Socket getSSLSocket(){
		SSLSocketFactory factory = null;
		try {
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks=null;

			try{
				ctx = SSLContext.getInstance("TLSv1.2");
			}catch (Exception e) {
				ctx = SSLContext.getInstance("TLS");
			}
			
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
			ks = KeyStore.getInstance("JKS");
			ks.load(null,null);
			kmf.init(ks, null);
			
			TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmFactory.init(ks);
			TrustManager[] trustManagers = tmFactory.getTrustManagers();
			X509TrustManager defaultTM = (X509TrustManager) trustManagers[0];
			X509TrustManager allowAll = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					if(true){
						return;
					}
					
					if(chain!= null && chain.length>0){
						for(X509Certificate cert : chain){
							cert.checkValidity();
						}
					}
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};
			
			ctx.init(kmf.getKeyManagers(),new TrustManager[]{allowAll},null);

			factory = ctx.getSocketFactory();
			return (SSLSocket)factory.createSocket(SERVER,PORT);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static int count = 0;
	private static long lastTime = System.currentTimeMillis() - 1000;

	static {
		new Thread() {
			public void run() {
				while (true) {
					System.out.println("EPS:" + (count *NUM_ES_PER_COLLECTOR)
							/ ((System.currentTimeMillis() - lastTime) / 1000));
					lastTime = System.currentTimeMillis();
					count = 0;
					try {
						sleep(10000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(threadNum==0){
						break;
					}
				}

			}
		}.start();
	}
}
