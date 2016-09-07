import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestHttpSentinelGet {
	
	public static void main(String[] args) throws Exception {
		String request = "https://atul6:8443/SentinelRESTServices/objects/sor/event/8BB707F1-3C5A-1034-AD93-0050568E5F2D/160731003509/160731003510";
		URL url = new URL(request);
		
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/json");

		connection.setRequestProperty("charset", "utf-8");
		connection.setUseCaches(false);
		
		String xaml = "zVpZk+I4En7nVxDMI1Htg8sQXTXhEwz4BnNsbFQIWxiDD7BkG/j1a0NXbVV1V2/v7sx43lBKyvzyUOoT8PX3cxjUM5ggP44eG9QXslGHkRO7fuQ9NuYz6YFp/P70FYEwoAcsQjDBxcJ6sSlCg5v0sZEm0SAGyEeDCIQQDbAzsFhlOqC/kAPwsqVRl4XHRttxIbOl6YdWr808tF2699DflJ8c6Dp9B7h0p12sRCiFcoQwiPBjgyap7gPZfyB7M4oc0N1Bm/xCtph1o26/wKZL2HdMZ/TY2GF8HBBEnudf8taXOPEImiQpYqlMLWcHQ9B4cehmKHl6fkYwwn4EAzbFOwsmRTyen78S7xZ9ddHA8r0I4DSB34y5nxkjCbJPFGtc5Hu/NV73QleOtvFtyIMojnwHBP4VlPFRIN7Fbp0NvDjx8S781AuKLBU/wLPz4FDt6LcG8R7aLyp6hzBB4AHtAPVNlwm3MCmqANbnpvzY+O2XsnbbOUtAhLZxEqL3w/8ODowyGMRH6D6gF6++Ift1hT8I1NNX6AzkyAlS5GdQLWv1CByIviUTOr+uqq4ncOufpz4qCvSMSnTEW3gfhvdoCL4HEf5f8vMmN3clNghS+LRCTc4UurYYetOwTbTOvAEoU53rJ+/xBuDt4pvgNbP34YeafK2h+w5XyG0gdnB721ngIY6Z8STY6frcnx+3eyCcgg0pn5fNM21zQBfHgbVWJwd1fQhySHssF6ygZKWgBal+irvEduotRpM+3p4j+ah6TSoI+04vaHagthQtIZ4dcEvXLX1vEtzEvChzze6mmd3v6MHOJtqxSOfF+TUystsLA96hiHAjQHBSyf0e82vrnOkjxKF9czQ5WJfhmrNFhtcDjV4wQ0E+7aKDOg56UmwFXkvhm92tbjUPeI6k1YLYLrE1tDiGovbX1NXV3ihX3WnaYpRhGKRTOraojmmNFsolU/suhvr63GH0rWY7ym6z4nRO6HAbtPHsw9b0m4FGLOFRFFnpFCm0lss66a7Omsl23cygvMfH19C/iXUZ/gm8vKZi2SH7AsDgdcCXbXRbNAwMnxRZFrgrz7Ow6bG5zLGeLE5daxzuWZXzDqfdwR/2c5JjDSSxAntVTDkX2ZVgG8ZEzIPWejG+rpdjer2Uz7XRjN1wnmpzrDKThhLpLnJ6Gp53q0hJndE4c0MmXdF9rPDsUpiJtCIccmUmttWZQ6lUvBKu8lmZrXJNYNs1ZeZR6nXeWux/bvVXjNZuVmWZk79zS5RYVuNZg2HLed6bFJ9FlszTDRbAxIRgZox3hMVPKTMZ9+QsbdeSKTXKufkkn+xPfXSW5km8GPLG3lvTvjfeK7FhJ8eRbwypjZiKSzPTRtF4OeQPvKYHQe8oHS5YGKUbeXThavLxKF2Frmnns0W4ACImYk8YjvXW1Nh19i2mh4XlpjumGNQKOSDutWXvcO0ziy1/Sdp7bC1icu5DPbfHcr/WZ/1gbvXBksMZeZhGR5tehMkQSrzik2wyGxEHspX2I4SUALYJwCOcsbATAjUJmS7Qjx2x0+90t0NSYmsSlNad6UxPVCWJNJUfU4bAr3umjK/ciRc7c2lBZHmaNs2QcfNYsj2kXiNB049KK9lKNu5nSzjMrR6bhrUT7m9FxmNWhiywBsvFFzEfXVm3zNzIaIuSZ8wlyHozCi17TH7BzViaLzjVmKpnxW7ngrEaT+K1vMsctVZkaMoZrOB5IscKQpgZGrf2+eXYA4Etc3uKlpGnheiKE3JJtkcXuGsGEl3oT4cUjxO9uzC7uu0JiMtrG5UjmQu56NNy3h4319qG7Vx80znQKGIOPEtPrjP9yB9XWX7VjIu+zrlQHqczIuwO1YmynF9ogzodV4vVmK01l5ArSoEkIRj2aKWXYc2MGZRsVpClkmMEZtHobKJkvBkOlaWUs4GhLTMlbo89z5tNcyVijKQTB8q81antiiz7EySdZXCNxnJXSL2o3SVIfnJ2T67ZGV2l9vqstU/DfEP29uDQNcUOFx6wnQlxuAvO+X6NFmxz0VHI2uLU8UfnfHiYriaC6tLGcDY5XnkGnvjNkhH3PnXZeruc1VbakFbBXuWMe3f52DFehfeeQrztNu+60QszstLNHjr4ZVhelrJQl4q7DODPGR/1hbpJfPdhe1s6SCN0hE4BA7qNJ+CGfvRCq+4qn16GHwzyceT6JS9CdTXGHCzUwZ9RwWKRFmkJu8UwebOO+bCOeKWyBdGLLFzEJiyo3ztpYRrD8w9lfFDw2eIOffop5XUGTrmuEL/x/sXPH6r70eR74fdQMU78TYrh5zP1MsSPDTMOIGp8N3u7d15Js/+fWfODf2PkDiyoNvIH+HKEJfcZoEJh5DWe2DK7BSFKAI6TV/DvzD19J/5e8n94qsMk9FH5JPgr/A1BBLzimeL4boEJfebxnw0DxUFaHhQBlmQZfhr5PxtHkkYmPMZJdZG4J4SPkwQGtzdVVUAyH+YqxNsgzst+WxWM6A5BT+KsKNHKCqOMhly0tCQCgZhVeVSebxdQ2Q+fq4Jwr9GKD4pbdGkLgsTZyVFxyRZdtSokxcM2gbeisIsqqSwgz2WNmuB2Wistj39nhnUceMRV4bjlhQ0KBllpXm5puZ0VE6I0qO7EAAf7Gaw0Fn54j0OlnQMWpHwSxXkAXQ9yAFXWOO599FahlQXjlfFokcBVBQKWzZN1bk+lqjA4CSzukMqZcFI8Dlz/74DEhZvUq8o4CArSV3GfKBt3+WsRrpL/liAEgHabGCRuZaG4d6rqcZTRsNOgeCGCjR/4+FJ1av5G98gcwaSyxNy4VsXn9R6GGfAqQ3B/JVbct28gSgQVH49bSUh+gH9Slh/Ff+g3abcnmQWdNCkicQfyF3yjhmA2+AdZn2n1zj//ELdfpl7+hPD0Lw==";
		connection.setRequestProperty("Authorization", "X-SAML " + xaml);
		
		SSLContext sslContext = SSLContext.getInstance("TLS");      
	    sslContext.init(null, new TrustManager[]{new X509TrustManager() {			
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {			
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {				
				}
			}
	    },null);	    
	    connection.setSSLSocketFactory(sslContext.getSocketFactory());
	    
	    connection.setHostnameVerifier(new HostnameVerifier() {			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		connection.disconnect();

	}

}
