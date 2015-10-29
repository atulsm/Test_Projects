package sentinel;

public class TestSentinelHostBean {

	public String protocol = "https";
	public String host = "164.99.175.163";
	public int port = 8443;
	public String user = "admin";
	public String password = "n";

	public static void main(String args[]) throws Exception {
		TestSentinelHostBean sentinel = new TestSentinelHostBean();
	}
/*
	private void get() throws Exception {
		for (int i = 1; i < 1000000; i++) {
			BeanTransporterSync<SentinelHostBean> beanTransporter = getTransporter();
			
			PagedBeanCollection<SentinelHostBean> list = beanTransporter.list();
			for (SentinelHostBean bean : list.getBeans()) {
				System.out.println(bean);
			}
			
			if (i % 100 == 0) {
				Thread.sleep(30000);
			}
		}

	}

	private BeanTransporterSync<SentinelHostBean> getTransporter()
			throws HttpException {
		// Set up the base of the URL we need to communicate with the
		// Sentinel server
		GenericURI sentinelBaseURI = new GenericURI();
		sentinelBaseURI.setScheme(protocol).setHost(host).setPort(port);
		// Create a synchronous requestor for simplicity
		JSONHttpRequestorJavaSync requestor = createRequestor(
				sentinelBaseURI.toString(), user, password);
		// Set up the base of the URLs we need to access data objects
		GenericURI objectsBaseURI = new GenericURI(sentinelBaseURI);
		objectsBaseURI.appendPathComponent("SentinelRESTServices")
				.appendPathComponent("objects");

		// Need a factory that will construct URLs for the transporters
		UriConstructorFactory uriFactory = new UriConstructorFactoryImpl(
				objectsBaseURI.toString());

		return new BeanTransporterJSONSync<SentinelHostBean>(
				SentinelHostBeanMetaData.getInstance(), requestor, uriFactory);
	}


	public static JSONHttpRequestorJavaSync createRequestor(
			String sentinelBaseURI, String userName, String userPassword)
			throws HttpException {
		GenericURI sentinelServerURI = new GenericURI(sentinelBaseURI);
		JSONHttpRequestorJavaSync requestor = new JSONHttpRequestorJavaSync();
		// NOTE: do NOT use the TestTrustManager for production - it doesn't
		// check TLS/SSL certs at all.
		requestor
				.setTrustManager(new JSONHttpRequestorJavaSync.TestTrustManager());
		requestor.authenticate(userName, userPassword, sentinelServerURI);
		return requestor;
	}
	*/
}
