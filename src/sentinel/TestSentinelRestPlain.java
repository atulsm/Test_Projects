package sentinel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestSentinelRestPlain {
	
	private static final String url = "https://164.99.175.165:8443/SentinelRESTServices/objects/host";
	//private static String token = "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";
	private static String token = "be03fa4a75648c879128505789a6dc6e4dd42b2772693771b78e6d8a07bffaf6";
	
	private static List<HttpURLConnection> connections = new ArrayList<>();
	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
				
		for(int i =0 ;i<1000;i++){
			HttpURLConnection	connection = getConnection();
			//connections.add(connection);
			String ret = read(connection);
			System.out.println(ret);	
		}				
		
		System.out.println("Took " + (System.currentTimeMillis() - start)/1000 + " seconds");
	}
	
	public static HttpURLConnection getConnection(){
		HttpURLConnection	connection = null;
		try
		{
			connection = (HttpURLConnection) new URL(url).openConnection();
			if (connection instanceof HttpsURLConnection)
			{
				HttpsURLConnection secureConnection = (HttpsURLConnection)connection;
				SSLContext	sslContext = SSLContext.getInstance("TLS");
				X509TrustManager trustall = new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkServerTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {}
					
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {}
				};
				
				sslContext.init(null, new TrustManager [] {trustall}, null);
				((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());				
				
				secureConnection.setHostnameVerifier(new HostnameVerifier() {					
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});
			}
			connection.setRequestProperty("Authorization", "X-SAML" + ' ' + getAuthMaterial());
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/json" + "; charset=" + "UTF-8");
			
			connection.setRequestMethod("GET");
			connection.setDoInput(true);			
		}catch(Exception e){
			e.printStackTrace();
		}
		return connection;
	}
	
	private static String read(HttpURLConnection connection) throws Exception{
		StringBuilder build = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String str;
		while( (str = br.readLine()) != null){
			build.append(str).append("\n");
		}
		
		return build.toString();
	}
	
	private static String getAuthMaterial(){
		return token;
	}
	
}
