import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class Client {
	private static final Random rnd = new Random();
	private static int EPS = 2;
	
	//private static double EPS_VARIENCE =0.5;
	private static double EPS_VARIENCE =1;
	private static int EPS_STICKY_TIME =5;	
	
	private static final int NUM_THREADS = 1; 
	private static final int NUM_ES_PER_COLLECTOR = 1;

	//private static final String SERVER = "ec2-52-90-235-91.compute-1.amazonaws.com";
	//private static final String SERVER = "164.99.175.163";
	private static final String SERVER = "idcdvuba101.ubadom100.lab";
	
	private static long MAX_EVENTS = 2;
	
	private static final int PORT = 1469;
	private static final boolean ssl = false;
	
	private static boolean sendOldData = false;
	private static final int sendFromDate = 10; //Send events dating from this day before today.
	private static final int sendToDate = 1; //Send events dating from this day before today.

	
	private static String[] IPS = {"1.6.0.0", //INDIA
		"41.66.0.0", //IVORY COST
		"2.60.0.0", //RUSSIA
		"139.82.0.0", //Brazil
		"23.16.0.0", //Canada
		""};
	
	
	private static final String[] msg = {
			"CEF:0|MicroFocus|Filr|3.4|100|Filr: Authentication Status |6|src=164.99.199.38 suser=adminq dst=164.99.197.72 msg=adminq  Unrecognized user [kablink,adminq] categoryObject=/Actor categoryBehavior=/Authentication categoryDeviceGroup=/User categoryOutcome=/Failure cat=Security categorySignificance=/Informational",
			//"CEF:1|ArcSight|ArcSight|7.5.0.7983.0|agent:050|Connector Raw Event Statistics|Low| eventId=12081 mrt=1517796161671 categorySignificance=\\/Informational categoryBehavior=\\/Execute\\/Response categoryDeviceGroup=\\/Application catdt=Security Management categoryOutcome=\\/Success categoryObject=\\/Host\\/Application art=1517796161679 cat=\\/Agent\\/RawEvent\\/Statistics deviceSeverity=Warning rt=1517796161671 fileType=Agent cs1=0.28213675213675216 cs2=109.66737891737891 cs3=0.0 cs4=0 cs5=0.0 cs6=3eaAZYGEBABCsMa6tgzLAdA\\\\=\\\\= cn1=9903 cn2=3849325 cn3=0 deviceCustomDate1=1517795861671 cs1Label=Event throughput cs2Label=Raw event character throughput cs3Label=Event throughput (SLC) cs4Label=Raw event length (SLC) cs5Label=Raw event character throughput (SLC) cs6Label=Destination ID cn1Label=Total event count cn2Label=Total raw event length cn3Label=Event count (SLC) deviceCustomDate1Label=Last time ahost=idcdvstl275 agt=10.204.102.241 agentZoneURI=\\/All Zones\\/ArcSight System\\/Private Address Space Zones\\/RFC1918: 10.0.0.0-10.255.255.255 amac=00-50-56-AB-06-F0 av=7.5.0.7983.0 atz=America\\/Denver at=sflow dvchost=idcdvstl275 dvc=10.204.102.241 deviceZoneURI=\\/All Zones\\/ArcSight System\\/Private Address Space Zones\\/RFC1918: 10.0.0.0-10.255.255.255 dvcmac=00-50-56-AB-06-F0 dtz=America\\/Denver _cefVer=1.0",
			//"CEF:0|Security|threatmanager2|1.0|100|worm successfully stopped|10|src=10.0.0.1 dst=2.1.2.2 spt=1232",
			"MSWinEventLog\t1\tSecurity\t78\tMon Aug 11 21:01:44 2014\t4624\tMicrosoft-Windows-Security-Auditing\tATUL-SAM\\Administrator\tN/A\tSuccess Audit\tATUL-SAM\tLogon\t\tAn account was successfully logged on.    Subject:   Security ID:  S-1-5-18   Account Name:  ATUL-SAM$   Account Domain:  WORKGROUP   Logon ID:  0x3e7    Logon Type:   10    New Logon:   Security ID:  S-1-5-21-976411532-455443073-982928273-500   Account Name:  Administrator   Account Domain:  ATUL-SAM   Logon ID:  0xb34c7   Logon GUID:  {00000000-0000-0000-0000-000000000000}    Process Information:   Process ID:  0x101c   Process Name:  C:\\Windows\\System32\\winlogon.exe    Network Information:   Workstation Name: ATUL-SAM   Source Network Address: 164.99.202.15   Source Port:  2506    Detailed Authentication Information:   Logon Process:  User32    Authentication Package: Negotiate   Transited Services: -   Package Name (NTLM only): -   Key Length:  0    This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. Workstation name is not always available and may be left blank in some cases.    The authentication information fields provide detailed information about this specific logon request.   - Logon GUID is a unique identifier that can be used to correlate this event with a KDC event.   - Transited services indicate which intermediate services have participated in this logon request.   - Package name indicates which sub-protocol was used among the NTLM protocols.   - Key length indicates the length of the generated session key. This will be 0 if no session key was requested.  This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. Workstation name is not always available and may be left blank in some cases.    The authentication information fields provide detailed information about this specific logon request.   - Logon GUID is a unique identifier that can be used to correlate this event with a KDC event.   - Transited services indicate which intermediate services have participated in this logon request.   - Package name indicates which sub-protocol was used among the NTLM protocols.   - Key length indicates the length of the generated session key. This will be 0 if no session key was requested. This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. \t57", //Windows collector 4000 chars

			"snort[4323]: [1:472:5] ICMP redirect host [Classification: Potentially Bad Traffic] [Priority: 2]: {ICMP} #IP1 -> #IP2 ", //Snort

			//"sshd[28817]: error: PAM: Authentication failure for user_tena1 from 23.16.0.1",
			
			"httpd: GET /tomcat.css HTTP/1.1 200 5926", //Collector is not changing date
			
			"%ASA-6-302014: Teardown TCP connection 7177 for management:164.99.17.109/42257 to NP Identity Ifc:164.99.17.6/443 duration 0:00:00 bytes 2698 TCP FINs", //CISCO			

			
			"usermod[32277]: delete `Testuser2' from group `newgroup1'", //RedHat Linux

			"sshd[28817]: error: PAM: Authentication failure for user_tena1 from #IP1",		


			//"snort[4323]: [1:472:5] ICMP redirect host [Classification: Potentially Bad Traffic] [Priority: 2]: {ICMP} 172.16.3.10 -> 172.16.1.177 ", //Snort


			//"snort[4323]: [1:472:5] ICMP redirect host [Classification: Potentially Bad Traffic] [Priority: 2]: {ICMP} 41.66.0.0 -> 1.6.0.0 ", //Snort
			

			//"sshd[28817]: error: PAM: Authentication failure for user_tena1 from 23.16.0.1",		


		"kernel: SUSE Linux Enterprise Server 10 (x86_64)", //SUSE Collector	
		//"sshd[28817]: error: PAM: Authentication failure for satul from 1.6.0.0", //Generic INDIA  			
		//"snort[4323]: [1:472:5] ICMP redirect host [Classification: Potentially Bad Traffic] [Priority: 2]: {ICMP} 172.16.3.10 -> 172.16.1.177 ", //Snort
		
		//"sshd[28817]: error: PAM: Authentication failure for user_tena1 from 23.16.0.1",		
		"%ASA-6-302014: Teardown TCP connection 7177 for management:164.99.17.109/42257 to NP Identity Ifc:164.99.17.6/443 duration 0:00:00 bytes 2698 TCP FINs", //CISCO


		//"sshd[28817]: error: PAM: Authentication failure for satul from 1.6.0.0", //Generic INDIA  			
			


	//	"MSWinEventLog\t1\tSecurity\t78\tMon Aug 11 21:01:44 2014\t4624\tMicrosoft-Windows-Security-Auditing\tATUL-SAM\\Administrator\tN/A\tSuccess Audit\tATUL-SAM\tLogon\t\tAn account was successfully logged on.    Subject:   Security ID:  S-1-5-18   Account Name:  ATUL-SAM$   Account Domain:  WORKGROUP   Logon ID:  0x3e7    Logon Type:   10    New Logon:   Security ID:  S-1-5-21-976411532-455443073-982928273-500   Account Name:  Administrator   Account Domain:  ATUL-SAM   Logon ID:  0xb34c7   Logon GUID:  {00000000-0000-0000-0000-000000000000}    Process Information:   Process ID:  0x101c   Process Name:  C:\\Windows\\System32\\winlogon.exe    Network Information:   Workstation Name: ATUL-SAM   Source Network Address: 164.99.202.15   Source Port:  2506    Detailed Authentication Information:   Logon Process:  User32    Authentication Package: Negotiate   Transited Services: -   Package Name (NTLM only): -   Key Length:  0    This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. Workstation name is not always available and may be left blank in some cases.    The authentication information fields provide detailed information about this specific logon request.   - Logon GUID is a unique identifier that can be used to correlate this event with a KDC event.   - Transited services indicate which intermediate services have participated in this logon request.   - Package name indicates which sub-protocol was used among the NTLM protocols.   - Key length indicates the length of the generated session key. This will be 0 if no session key was requested.\t57", //Windows collector 2000 chars
//		"MSWinEventLog\t1\tSecurity\t78\tMon Aug 11 21:01:44 2014\t4624\tMicrosoft-Windows-Security-Auditing\tATUL-SAM\\Administrator\tN/A\tSuccess Audit\tATUL-SAM\tLogon\t\tAn account was successfully logged on.    Subject:   Security ID:  S-1-5-18   Account Name:  ATUL-SAM$   Account Domain:  WORKGROUP   Logon ID:  0x3e7    Logon Type:   10    New Logon:   Security ID:  S-1-5-21-976411532-455443073-982928273-500   Account Name:  Administrator   Account Domain:  ATUL-SAM   Logon ID:  0xb34c7   Logon GUID:  {00000000-0000-0000-0000-000000000000}    Process Information:   Process ID:  0x101c   Process Name:  C:\\Windows\\System32\\winlogon.exe    Network Information:   Workstation Name: ATUL-SAM   Source Network Address: 164.99.202.15   Source Port:  2506    Detailed Authentication Information:   Logon Process:  User32    Authentication Package: Negotiate   Transited Services: -   Package Name (NTLM only): -   Key Length:  0    This event is generated when a logon session is created. \t57", //Windows collector 1000 chars
		//"MSWinEventLog	1	Security	16	Tue May 28 16:09:02 2013	680	Security	SYSTEM	User	Success Audit	ATUL-LAPTOP	Account Logon		Logon attempt by: MICROSOFT_AUTHENTICATION_PACKAGE_V1_0    Logon account:  admin    Source Workstation: ATUL-LAPTOP    Error Code: 0x0    	9", //Windows collector 300 chars		
	//	"id=firewall sn=0017C56787F4 time=\"2012-06-27 12:01:05\" fw=38.104.128.38 pri=6 c=262144 m=98 msg=\"Connection Opened\" n=141496780 src=10.10.5.31:55439:X0 dst=10.10.7.40:53:X2 proto=udp/dns GGCDallas", //Sonicwall

		"host1 kernel: SUSE Linux Enterprise Server 10 (x86_64)", //SUSE Collector	
		"kernel: [ID 702911 user.notice] SunOS 5.10", //Oracle Solaris
		"kernel: AIX Some random aix os message", //IBM AIX
		"%ASA-6-302014: Teardown TCP connection 7177 for management:164.99.17.109/42257 to NP Identity Ifc:164.99.17.6/443 duration 0:00:00 bytes 2698 TCP FINs", //CISCO
		"sshd[4864]: SSH: Server;Ltype: Kex;Remote: 10.9.40.180-4494;Enc: aes256-ctr;MAC: hmac-sha1;Comp: none", //HP-UX
		"usermod[32277]: delete `Testuser2' from group `newgroup1'", //RedHat Linux
		"8\t3\t00000002-0002-0002-0002-000000000990\t00000001-0001-0001-0001-000000000990\t0990: HTTP: Webstore Exploit\t990\thttp\t10.10.10.17\t62165\t172.16.0.11\t80\t1\t0\t0\ttipping-ips\t17107965\t1238497945044", //Tippingpoint SMS
		"BlueCoat: Sample bluecoat message stateful log",
	};
	private static int threadNum =0;
	private static AtomicLong globalCount = new AtomicLong(0);
	
	private static long lastEPSChange = System.currentTimeMillis();
	private static long lastEPS = EPS;
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				System.out.println("Total events send: " + globalCount.get());
			}
		});
	}
	
	
	public static void main(String[] args) throws Exception{
		if(args.length == 1){
			EPS = Integer.parseInt(args[0]);
		}
		
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
								String msg = null;
								if(!sendOldData){
									//writer.write("MachineTest"+j+" " +message +  " AtulCount" + globalCount.getAndIncrement() + "\n");
									//writer.write("MachineTest"+globalCount.getAndIncrement()+" " +message +  " AtulCount" + globalCount.getAndIncrement() + "\n");
									msg = "MachineTest"+j+" " +message +/*  " AtulHost" + globalCount.getAndIncrement() + */"\n";																	
								}else{
									msg = getDate() + " MachineTest"+"."+j+" " +  message +  " AtulCount" + globalCount.getAndIncrement() + "\n";
								}
								
								msg = msg.replace("#IP1", getRandomIP());
								msg = msg.replace("#IP2", getRandomIP());
								
								writer.write(msg);
								writer.flush();
								
								if(globalCount.get() == MAX_EVENTS){
									System.out.println("Max events to send reached");
									System.exit(0);
								}
							}
							if(writer.checkError()){
								System.out.println("Error in sending");
								break;
							}
							count ++;
							localCount++;
							
							long time1 = System.currentTimeMillis();

							//check if 1 sec has passed
							if((time1-time) >= 1000){
								time = System.currentTimeMillis();
								localCount=0;
							}else if(localCount > getEPS()){
								sleep(1100 - (time1 - time));
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
	
	private static long getEPS(){
		if(EPS_VARIENCE == 1.0){		
			return EPS;
		}else{
			if( (System.currentTimeMillis() - lastEPSChange) > EPS_STICKY_TIME*1000){
				if(getRandomDay(0, 2) == 1){
					lastEPS = Math.round(lastEPS + (lastEPS*(1-EPS_VARIENCE)));
				}else{
					lastEPS = Math.round(lastEPS - (lastEPS*(1-EPS_VARIENCE)));
				}
				
				if(lastEPS < EPS/10 || lastEPS > EPS*10 || EPS == 0){
					lastEPS=EPS;
				}
				System.out.println("EPS changed to " + lastEPS);
				lastEPSChange = System.currentTimeMillis();
			}			
			return lastEPS;
			
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
	
	private static Random r = new Random();
	
	private static String getDate(){
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE,(0-getRandomDay(sendToDate,sendFromDate)));
		cal.set(Calendar.HOUR_OF_DAY,getRandomDay(0,24) );
		cal.set(Calendar.MINUTE,getRandomDay(0,60) );
		Date date = cal.getTime();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateStr;
	}
	
	//return a number from 0 and end
	private static  int getRandomDay(int start, int end){
		int R = r.nextInt(end-start) + start;
		return R;
	}
	
	private static String getRandomIP(){
		Random r = new Random();
		return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
	}
}
