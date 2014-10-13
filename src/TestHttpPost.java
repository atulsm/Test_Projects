import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestHttpPost {

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 100; i++) {
			connect();
		}
	}

	public static void connect() throws Exception {
		 String payload = "[{\"utctime\":\"1392026411\",\"cid\":\"{68E5E295-4D9E-4AAB-B386-6EC9A93B3546}\",\"lgid\":\"{F6DA1507-12AF-11D3-AB21-00A0C98620CE}\",\"cname\":\"ATUL-SAM\",\"lgname\":\"Application\",\"tz\":\"Asia/Calcutta\",\"cmethod\":\"SYSLOG\",\"appid\":\"NQ-AgentManager\",\"syslogtime\":\"Feb 10 2014 10:00:11\",\"evt\":\"NQ-AgentManager: {\\\"f_807\\\":\\\"Application\\\",\\\"f_560\\\":\\\"21241\\\",\\\"f_564\\\":\\\"The central computer detected a change to the rules for one or more computers, and will begin downloading the new rules and configuration settings to the affected computers.\\\",\\\"f_645\\\":\\\"ATUL-SAM\\\",\\\"f_503\\\":\\\"2\\\\\\/10\\\\\\/2014 10:00:11 AM\\\",\\\"f_562\\\":\\\"Low\\\",\\\"f_502\\\":\\\"164.99.175.166\\\",\\\"f_561\\\":\\\"Agent Manager\\\",\\\"f_579\\\":\\\"Information\\\",\\\"f_500\\\":\\\"NetIQ\\\",\\\"f_501\\\":\\\"Agent Manager\\\",\\\"f_2062\\\":\\\"Agent Manager\\\",\\\"f_401\\\":\\\"{965BDE08-35A6-4AFB-8CDA-B0CB072CD8EC}\\\",\\\"f_402\\\":\\\"330\\\",\\\"f_406\\\":\\\"{68E5E295-4D9E-4AAB-B386-6EC9A93B3546}\\\",\\\"f_407\\\":\\\"{68E5E295-4D9E-4AAB-B386-6EC9A93B3546}\\\"}\"}]\n";
		//String payload = "Test string\n\n";
		String request = "http://164.99.175.165:1590/events";
		URL url = new URL(request);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length",
				String.valueOf(payload.length()));
		connection.setRequestProperty("charset", "utf-8");
		connection.setUseCaches(false);

		// connection.connect();
		OutputStream os = connection.getOutputStream();
		os.write(payload.getBytes());
		os.close();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		connection.disconnect();

	}

}
