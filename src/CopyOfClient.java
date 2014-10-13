import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class CopyOfClient {	
	private static final Random rnd = new Random();
	private static final int EPS = 2000;
	private static final int NUM_THREADS = 3; 
	private static final int NUM_ES_PER_COLLECTOR = 5;

	//private static final String SERVER = "192.168.1.4";
	private static final String SERVER = "164.99.175.165";
	private static final int PORT = 1468;
	private static final boolean ssl = false;
	
	private static final String[] msg = {
		"MSWinEventLog	1	Security	16	Tue May 28 16:09:02 2013	680	Security	SYSTEM	User	Success Audit	ATUL-LAPTOP	Account Logon		Logon attempt by: MICROSOFT_AUTHENTICATION_PACKAGE_V1_0    Logon account:  admin    Source Workstation: ATUL-LAPTOP    Error Code: 0x0    	9", //Windows collector
		"id=firewall sn=0017C56787F4 time=\"2012-06-27 12:01:05\" fw=38.104.128.38 pri=6 c=262144 m=98 msg=\"Connection Opened\" n=141496780 src=10.10.5.31:55439:X0 dst=10.10.7.40:53:X2 proto=udp/dns GGCDallas", //Sonicwall
		"snort[4323]: [1:472:5] ICMP redirect host [Classification: Potentially Bad Traffic] [Priority: 2]: {ICMP} 172.16.3.9 -> 172.16.1.177", //Snort
		"id=firewall sn=0017C56787F4 time=\"2012-06-27 12:01:05\" fw=38.104.128.38 pri=6 c=262144 m=98 msg=\"Connection Opened\" n=141496780 src=10.10.5.31:55439:X0 dst=10.10.7.40:53:X2 proto=udp/dns GGCDallas", //Sonicwall
		"kernel: SUSE Linux Enterprise Server 10 (x86_64)", //SUSE Collector	
		"kernel: [ID 702911 user.notice] SunOS 5.10", //Oracle Solaris
		"kernel: AIX Some random aix os message", //IBM AIX
		//"sshd[28817]: error: PAM: Authentication failure for dcorlette from 130.57.171.51", //Generic
		"%ASA-6-302014: Teardown TCP connection 7177 for management:164.99.17.109/42257 to NP Identity Ifc:164.99.17.6/443 duration 0:00:00 bytes 2698 TCP FINs", //CISCO
		//"sshd[4864]: SSH: Server;Ltype: Kex;Remote: 10.9.40.180-4494;Enc: aes256-ctr;MAC: hmac-sha1;Comp: none", //HP-UX
		//"usermod[32277]: delete `Testuser2' from group `newgroup1'", //RedHat Linux
		"8\t3\t00000002-0002-0002-0002-000000000990\t00000001-0001-0001-0001-000000000990\t0990: HTTP: Webstore Exploit\t990\thttp\t10.10.10.17\t62165\t172.16.0.11\t80\t1\t0\t0\ttipping-ips\t17107965\t1238497945044", //Tippingpoint SMS
		//"BlueCoat: Sample bluecoat message stateful log",
	};
	private static int threadNum =0;
	
	
	public static void main(String[] args) throws Exception{
			Thread.currentThread().sleep(100);
			new Thread(){
				public void run(){
					String message = null;
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
								for(int i=0;i<NUM_THREADS;i++){
									message = msg[i];
									//writer.write("EventSource"+local+"."+j+" " +message +  "\n");
									writer.write("Machine"+"."+j+" " +message +  "\n");
									//writer.write("164.99.135.1 " + message +  "\n");
									//writer.write("testAllow " + message +  "\n");
									writer.flush();
								}
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
							}else if(localCount > EPS+rnd.nextInt(300)){
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
