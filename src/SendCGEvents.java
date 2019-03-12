
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import com.jcraft.jsch.Logger;

/**
 * Referred from https://github.com/atulsm/https-multipart-purejava
 *
 */
public class SendCGEvents {
	
	private static final String ip = "localhost";
	private static final String port = "8443";
	private static final String userName = "admin";
	private static String password = "n";
	
	//private static String body = "{\"xdasclass\":7,\"xdasid\":5,\"xdasoutcome\":0,\"xdasdetail\":0,\"xdasoutcomename\":\"XDAS_OUT_SUCCESS\",\"xdasprov\":0,\"xdasreg\":0,\"xdastaxname\":\"XDAS_AE_MODIFY_DATA_ITEM_CONTENTS\",\"bgnt\":1503981001000,\"dt\":1503981001000,\"endt\":1503981001000,\"msg\":\"1.txt File integrity changed by 'corpdom\\\\satul' with outcome Success\",\"evt\":\"File integrity was changed\",\"rv42\":\"microfocus.com\",\"sip\":\"164.99.135.25\",\"shn\":\"ind-satul\",\"sp\":\"e:\\\\install\\\\Notepad++\\\\notepad++.exe\",\"sun\":\"satul\",\"rv35\":\"microfocus.com\",\"iuid\":\"S-1-5-21-1994326832-1066739575-5522801-76079\",\"rv150\":\"Operating System\",\"obsdom\":\"microfocus.com\",\"sn\":\"ind-satul\",\"obsip\":\"164.99.135.25\",\"obssvcname\":\"Change Guardian for Windows\",\"polid\":\"file_integrity1\",\"rc\":2,\"rv41\":\"microfocus.com\",\"dhn\":\"ind-satul\",\"dip\":\"164.99.135.25\",\"dp\":\"OS:Microsoft Windows 7 Professional Service Pack 1 (build 7601), 64-bit\",\"rv36\":\"D:\\\\tmp\\\\cg_test\",\"fn\":\"1.txt\",\"tdspace\":\"D:\",\"id\":\"61B842CB-D509-0C42-AAC9-8BF6D09B1747\",\"pn\":\"NetIQ Change Guardian\",\"rv32\":\"CHA\",\"st\":\"N\",\"id\":\"61B842CB-D509-0C42-AAC9-8BF6D09B1747\",\"sessid\":\"61B842CB-D509-0C42-AAC9-8BF6D09B1747\",\"estz\":\"GMT+0530\",\"dt\":1503981001000,\"det\":1503981001000,\"rv40\":\"5.0.0\",\"voc\":\"Unmanaged\",\"sev\":5,\"polid\":\"file_integrity1\",\"ei\":\"{}\"}";
	private static volatile String body = "{\"xdasclass\":7,\"xdasid\":5,\"xdasoutcome\":0,\"xdasdetail\":0,\"xdasoutcomename\":\"XDAS_OUT_SUCCESS\",\"xdasprov\":0,\"xdasreg\":0,\"xdastaxname\":\"XDAS_AE_MODIFY_DATA_ITEM_CONTENTS\",\"bgnt\":##DT##,\"dt\":##DT##,\"endt\":##DT##,\"msg\":\"1.txt File integrity changed by 'corpdom\\\\satul' with outcome Success\",\"evt\":\"File integrity was changed\",\"rv42\":\"microfocus.com\",\"sip\":\"164.99.135.25\",\"shn\":\"ind-satul\",\"sp\":\"e:\\\\install\\\\Notepad++\\\\notepad++.exe\",\"sun\":\"satul\",\"rv35\":\"microfocus.com\",\"iuid\":\"S-1-5-21-1994326832-1066739575-5522801-76079\",\"rv150\":\"Operating System\",\"obsdom\":\"microfocus.com\",\"sn\":\"ind-satul\",\"obsip\":\"164.99.135.25\",\"obssvcname\":\"Change Guardian for Windows\",\"polid\":\"file_integrity1\",\"rc\":2,\"rv41\":\"microfocus.com\",\"dhn\":\"ind-satul\",\"dip\":\"164.99.135.25\",\"dp\":\"OS:Microsoft Windows 7 Professional Service Pack 1 (build 7601), 64-bit\",\"rv36\":\"D:\\\\tmp\\\\cg_test\",\"fn\":\"1.txt\",\"tdspace\":\"D:\",\"id\":\"61B842CB-D509-0C42-AAC9-8BF6D09B1747\",\"pn\":\"NetIQ Change Guardian\",\"rv32\":\"CHA\",\"st\":\"N\",\"id\":\"61B842CB-D509-0C42-AAC9-8BF6D09B1747\",\"sessid\":\"61B842CB-D509-0C42-AAC9-8BF6D09B1747\",\"estz\":\"GMT+0530\",\"det\":##DT##,\"rv40\":\"5.0.0\",\"voc\":\"Unmanaged\",\"sev\":5,\"polid\":\"file_integrity1\",\"ei\":\"{}\"}";
	private static String diff = "@@ -2,3 +2,4 @@\r\n" + 
			"line2\r\n" + 
			"line3\r\n" + 
			"line4\r\n" + 
			"+line 5\r\n";
	private static String delta = "{\"schema\":[{\"iqcimSetting\":\"string\"},{\"iqcimAction\":\"string\"},{\"iqcimChanges\":\"table\"}],\"rows\":[[\"D:\\\\tmp\\\\cg_test\\\\1.txt\",\"Overwrite(1) and Write(1)\",{\"schema\":[],\"rows\":[]}]]}";
	public static String token;

	public static long count = 1000;
	public static long uploaded = 0;
	public static long logtime = System.currentTimeMillis();
	
	public static void main(String[] args) throws Exception{
		password = args[0];
		count = Integer.parseInt(args[1]);
		
		SendCGEvents uploader = new SendCGEvents();
		long start = System.currentTimeMillis();
		for(int i=0;i<count;i++) {
			if(uploaded != 0 && uploaded%1000==0) {
				long time = (System.currentTimeMillis() - logtime)/1000;				
				System.out.println("Forwarded " + uploaded + " events with eps " + 1000/time);
				logtime = System.currentTimeMillis();
			}
			uploader.main1(null);
		}
		long end = System.currentTimeMillis();
		long time = (end-start)/1000;
		
		System.out.println("Took " + time + " seconds to upload " + count + " events at " + count/time + " eps");
	}
	
	public static void main1(String[] args) throws Exception{		
		if(token == null) {
			//return;
		}
		
		String url = "https://" + ip + ":" + port + "/httpsink/events";
		MultipartBuilder multipart = new MultipartBuilder(url,token);
		UUID uuid = UUID.randomUUID();
		body = body.replaceAll("61B842CB-D509-0C42-AAC9-8BF6D09B1747", uuid.toString());
		body = body.replaceAll("##DT##", System.currentTimeMillis()+"");
		
        multipart.addFormField("entity", "", "application/json",body);
        //multipart.addFormField("attachment", "urn:sentinel:attachment:provider:cg#delta", "application/octet-stream",delta);
        //multipart.addFormField("attachment", "urn:sentinel:attachment:provider:cg#diff", "application/octet-stream",diff);         
        List<String> response = multipart.finish();

        for (String line : response) {
            System.out.println(line);
        }		
        //System.out.println("Completed data upload");
		uploaded++;
	}
		
	private static String getAuthToken() throws Exception {
		URL obj = new URL(new StringBuilder().append("https://").append(ip).append(":").append(port).append("/SentinelAuthServices/auth/tokens").toString());
		HttpsURLConnection  con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic "+ DatatypeConverter.printBase64Binary((userName+":"+password).getBytes()));
		con.setRequestProperty("Accept", "application/json");
		
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code for connection " + con.getURL() + ": " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			String res = response.toString();
			System.out.println(res);
			int end = res.indexOf("\",\"TokenDigest\":");
			if(end ==-1) {
				end = res.indexOf("\"}");
			}
			String token = res.substring(res.indexOf("\"Token\":\"")+9,end);
			return token;
		} else {
			System.out.println("POST request not worked");
			return null;
		}
	}	
	
    static {
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {					
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}				
					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// Do nothing. Just allow them all.				
					}				
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// Do nothing. Just allow them all.						
					}
				}
        };

        HostnameVerifier trustAllHostnames = new HostnameVerifier() {			
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
            token = getAuthToken();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }

    }
    
    private static class MultipartBuilder {
        private final String boundary;
        private static final String NEW_LINE = "\r\n";
        private HttpURLConnection conn;
        private String charset = "UTF-8";
        private OutputStream outputStream;
        private PrintWriter writer;
     
        public MultipartBuilder(String requestURL, String token) throws IOException {             
            // creates a unique boundary based on time stamp
            boundary = "---" + System.currentTimeMillis() + "---";
             
            URL url = new URL(requestURL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);     
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Authorization", "X-SAML "+token);
            
            outputStream = conn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),true);
        }
     
        public void addFormField(String name, String fileName, String contentType, String value) {
            writer.append("--" + boundary).append(NEW_LINE);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"")
                    .append(NEW_LINE);
            writer.append("Content-Type: ").append(contentType).append(NEW_LINE);
            writer.append(NEW_LINE);
            writer.append(value).append(NEW_LINE);
            writer.flush();
        }
              
        public List<String> finish() throws IOException {
            List<String> response = new ArrayList<String>();
     
            writer.append(NEW_LINE).flush();
            writer.append("--" + boundary + "--").append(NEW_LINE);
            writer.close();
     
            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                conn.disconnect();
            } else {
                //throw new IOException("Server returned status: " + status);
            	System.out.println("Server returned status: " + status);
            	try {
            		Thread.sleep(10000);
            	}catch (Exception e) {
					e.printStackTrace();
				}
            }     
            return response;
        }
    }
}

